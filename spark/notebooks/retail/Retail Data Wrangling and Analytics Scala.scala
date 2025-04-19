// Databricks notebook source
// MAGIC %md
// MAGIC # Retail Data Wrangling and Analytics

// COMMAND ----------

val file_location = "/FileStore/tables/retail.csv"

val retailDF = spark.read
  .format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load(file_location)

retailDF.printSchema()


// COMMAND ----------

retailDF.show()

// COMMAND ----------

// MAGIC %md
// MAGIC # Total Invoice Amount Distribution

// COMMAND ----------

import org.apache.spark.sql.functions._
import org.apache.spark.sql._
import spark.implicits._

val amountDF = retailDF
  .withColumn("amount", round( col("quantity") * col("unit_price"),2))
val totalAmountDF = amountDF
  .groupBy("invoice_no")
  .agg(round(sum("amount"),2).alias("total_amount"))
  .filter(col("total_amount") > 0)
  .orderBy(col("invoice_no").asc)


val quantile85 = totalAmountDF.stat.approxQuantile("total_amount", Array(0.85), 0.01)(0)
val quantile85DF = totalAmountDF.filter(col("total_amount") < quantile85)
quantile85DF.show()


// COMMAND ----------

def show_distribution(amount: DataFrame): Unit = {

  val minVal = amount.agg(min("total_amount")).first()(0)
  val maxVal = amount.agg(max("total_amount")).first()(0)
  val meanVal = amount.agg(mean("total_amount")).first()(0)
  val medianVal = amount.stat.approxQuantile("total_amount", Array(0.5), 0.01)(0)
  val modeVal = amount.groupBy("total_amount").count().orderBy(desc("count")).first()(0)

  println(f"Minimum: $minVal")
  println(f"Mean: $meanVal")
  println(f"Median: $medianVal")
  println(f"Mode: $modeVal")
  println(f"Maximum: $maxVal")

  display(amount)
}

val totalAmount = totalAmountDF.select(col("total_amount"))
show_distribution(totalAmount)

// COMMAND ----------

val totalAmount = quantile85DF.select(col("total_amount"))

show_distribution(totalAmount)

// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Placed and Canceled Orders

// COMMAND ----------

val cancellationDF = amountDF
  .withColumn("isCancelled", col("invoice_no").startsWith("C"))
  .withColumn("ym", date_format(col("invoice_date"), "yyyyMM"))

val cancelledDF = cancellationDF
  .filter(col("isCancelled") === true)
  .groupBy("ym")
  .agg(countDistinct("invoice_no").alias("cancelled_count"))

val orderDF = cancellationDF
  .filter(col("isCancelled") === false)
  .groupBy("ym")
  .agg(countDistinct("invoice_no").alias("order_count"))

val finalDF = orderDF.join(cancelledDF, "ym", "outer")
display(finalDF)


// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Sales

// COMMAND ----------

val dateDF = amountDF.withColumn("ym", date_format(col("invoice_date"), "yyyyMM"))
var monthlySalesDF = dateDF.groupBy("ym").agg(
    round(sum("amount"), 2).alias("Sales(Million)")
)

display(monthlySalesDF)

// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Active Users

// COMMAND ----------

var userDF = retailDF.withColumn("ym", date_format(col("invoice_date"), "yyyyMM"))
userDF = userDF.groupBy("ym").agg(countDistinct("customer_id"))
display(userDF)

// COMMAND ----------

// MAGIC %md
// MAGIC # New and Existing Users

// COMMAND ----------

val retailDFWithYM = retailDF.withColumn("ym", date_format(col("invoice_date"), "yyyyMM"))

// Get first purchase month for each customer
val firstPurchaseDF = retailDFWithYM.groupBy("customer_id")
  .agg(min("ym").alias("first_purchase_ym"))

// Join first purchase info back to retailDF
val retailWithFirstPurchase = retailDFWithYM.join(
  firstPurchaseDF, Seq("customer_id"), "left"
)

// Filter new users: ym == first_purchase_ym
val newUserDF = retailWithFirstPurchase
  .filter(col("ym") === col("first_purchase_ym"))

val newUserCountDF = newUserDF.groupBy("ym")
  .agg(countDistinct("customer_id").alias("new_user_count"))

// Filter existing users: ym > first_purchase_ym
val existingUserDF = retailWithFirstPurchase
  .filter(col("ym") > col("first_purchase_ym"))

val existingUserCountDF = existingUserDF.groupBy("ym")
  .agg(countDistinct("customer_id").alias("ex_user_count"))

// Merge new and existing user counts
val userCountDF = newUserCountDF
  .join(existingUserCountDF, Seq("ym"), "outer")
  .na.fill(0)

display(userCountDF.orderBy("ym"))

// COMMAND ----------

// MAGIC %md
// MAGIC ## Finding RFM
// MAGIC
// MAGIC RFM is a method used for analyzing customer value. It is commonly used in database marketing and direct marketing and has received particular attention in the retail and professional services industries. ([wikipedia](https://en.wikipedia.org/wiki/RFM_(market_research)))
// MAGIC
// MAGIC
// MAGIC RFM stands for three dimensions:
// MAGIC
// MAGIC - Recency – How recently did the customer purchase?
// MAGIC
// MAGIC - Frequency – How often do they purchase?
// MAGIC
// MAGIC - Monetary Value – How much do they spend?

// COMMAND ----------

val current_date = to_date(lit("2025-02-05"))

// Recency
val recencyDF = amountDF.groupBy("customer_id")
  .agg(max("invoice_date").alias("last_purchase_date"))
  .withColumn("recency", datediff(current_date, col("last_purchase_date")))
  .select("customer_id", "recency")

// Frequency
val frequencyDF = retailDF.groupBy("customer_id")
  .agg(countDistinct("invoice_no").alias("frequency"))

// Monetary
val monetaryDF = amountDF.groupBy("customer_id")
  .agg(round(sum("amount"), 2).alias("monetary"))

// Join all
var rfmDF = recencyDF
  .join(frequencyDF, Seq("customer_id"), "inner")
  .join(monetaryDF, Seq("customer_id"), "inner")

display(rfmDF)


// COMMAND ----------

// MAGIC %md
// MAGIC # RFM Segmentation

// COMMAND ----------

import org.apache.spark.sql.expressions._

val recencyWindow = Window.orderBy(col("recency").asc)
val frequencyWindow = Window.orderBy(col("frequency").desc)
val monetaryWindow = Window.orderBy(col("monetary").desc)

rfmDF = rfmDF.withColumn("recency_score", ntile(5).over(recencyWindow))
rfmDF = rfmDF.withColumn("frequency_score", ntile(5).over(frequencyWindow))
rfmDF = rfmDF.withColumn("monetary_score", ntile(5).over(monetaryWindow))

// Concatenate scores to get RFM score
rfmDF = rfmDF.withColumn(
  "rfm_score",
  concat_ws("",
    col("recency_score").cast("string"),
    col("frequency_score").cast("string"),
    col("monetary_score").cast("string")
  )
)

display(rfmDF)


// COMMAND ----------

val rfmWithSegment = rfmDF.withColumn(
  "rf_segment",
  concat_ws("", col("recency_score").cast("string"), col("frequency_score").cast("string"))
)

val rfmWithLabels = rfmWithSegment.withColumn("segment",
  when(col("rf_segment").isin("11", "12", "21", "22"), "Hibernating")
    .when(col("rf_segment").isin("13", "14", "23", "24"), "At Risk")
    .when(col("rf_segment") === "25", "Can't Lose")
    .when(col("rf_segment").isin("31", "32"), "About to Sleep")
    .when(col("rf_segment") === "33", "Need Attention")
    .when(col("rf_segment").isin("34", "35", "44", "45"), "Loyal Customers")
    .when(col("rf_segment") === "41", "Promising")
    .when(col("rf_segment") === "51", "New Customers")
    .when(col("rf_segment").isin("42", "43", "52", "53"), "Potential Loyalists")
    .when(col("rf_segment").isin("54", "55"), "Champions")
    .otherwise("Other")
)

display(rfmWithLabels)
