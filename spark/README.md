# Introduction

This repository is divided into three parts:

1. London Gift Shop (LGS) Proof of Concept (POC):
   A marketing-focused analysis for the e-commerce store London Gift Shop to help understand customer shopping patterns. Using RFM (Recency, Frequency, Monetary) metrics from the PostgreSQL user database, customers were segmented into categories such as hibernating, loyal customers, and potential loyalists. The data was loaded into the Databricks File System (DBFS) and analyzed using PySpark, Scala, and Databricks notebooks.

2. GCP Dataproc Analysis:
   This section utilizes Google Cloud Platform's Dataproc to spin up a Hadoop cluster running Spark and Zeppelin notebooks. The goal was to analyze global development datasets, including extracting GDP and other macroeconomic indicators.

3. Fundamental ETL and Analysis:
   Located in the fundamental directory, this part showcases ETL pipelines and analytical work built using PySpark and Jupyter notebooks.

# London Gift Shop (LGS) Proof of Concept (POC)

This section focuses on customer segmentation such as using RFM (Recency, Frequency, Monetary) analysis to better understand customer shopping patterns for the London Gift Shop. The analysis was performed using data from a PostgreSQL database, which was loaded into the Databricks File System (DBFS) for processing. PySpark and Scala were used to implement the RFM model, and Databricks notebooks were utilized for visualization and insights.

### Key Features:

- **Technologies Used:** PySpark, Scala, and Databricks notebooks.
- **Data Source:** PostgreSQL user database.

### Relevant Files:

- [Retail Data Wrangling and Analytics Scala.scala](notebooks/retail/Retail%20Data%20Wrangling%20and%20Analytics%20Scala.scala)
- [Retail Data Wrangling and Analytics Python.ipynb](notebooks/retail/Retail%20Data%20Wrangling%20and%20Analytics%20Python.ipynb)

# GCP Dataproc Analysis

This section demonstrates the use of Google Cloud Platform's Dataproc to analyze global development datasets. A Hadoop cluster was spun up using Dataproc, and Spark jobs were executed to process and analyze data such as GDP and other macroeconomic indicators. Zeppelin notebooks were used for interactive data exploration and visualization.

### Key Features:

- **Cluster Management:** Automated provisioning of Hadoop clusters using GCP Dataproc.
- **Visualization:** Visualization done by Zeppelin notebooks.

### Relevant Files:

- [WDI Data Analytics.zpln](notebooks/WDI%20Data%20Analytics.zpln)

# Fundamental ETL and Analysis

This section provides foundational examples of ETL pipelines and data analysis using PySpark. It includes hands-on exercises for data ingestion, transformation, and analysis. The focus is on building scalable ETL jobs and performing data manipulation using Spark DataFrames.

### Relevant Files:

- [ETL pgexercieses CSV files.ipynb](notebooks/fundamental/ETL%20pgexercieses%20CSV%20files.ipynb)
- [Spark Dataframe Data Manipulation.ipynb](notebooks/fundamental/Spark%20Dataframe%20Data%20Manipulation.ipynb)
- [Spark ETL Jobs.ipynb](notebooks/fundamental/Spark%20ETL%20Jobs.ipynb)

# Improvements

In the future, regression analysis can be applied to the RFM data to predict hypothetical scores for users. This approach can help understand how changes in customer behavior might affect their RFM scores and allow LGS to target specific groups of individuals more effectively. By leveraging predictive analytics, LGS can enhance their marketing strategies and improve customer retention.
