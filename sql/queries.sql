-- Query 1: Insert spa facility into facilities table
INSERT INTO cd.facilities 
VALUES 
  (9, 'Spa', 20, 30, 100000, 800);

-- Query 2: Insert spa facility into facilities table w/ automatic incrementing id
INSERT INTO cd.facilities 
VALUES 
  (
    (
      SELECT 
        max(facid) 
      from 
        cd.facilities
    ) + 1, 
    'Spa', 
    20, 
    30, 
    100000, 
    800
  );

-- Query 3: Fixing the data for the second tennis court
UPDATE 
  cd.facilities 
SET 
  initialoutlay = 10000 
WHERE 
  name = 'Tennis Court 2';

-- Query 4: Update second tennis court so that it costs 10% more than the first
UPDATE 
  cd.facilities 
SET 
  membercost = (
    SELECT 
      membercost * 1.1 
    FROM 
      cd.facilities 
    WHERE 
      name = 'Tennis Court 1'
  ), 
  guestcost = (
    SELECT 
      guestcost * 1.1 
    FROM 
      cd.facilities 
    WHERE 
      name = 'Tennis Court 1'
  ) 
WHERE 
  name = 'Tennis Court 2';

-- Query 5: Delete all bookings
DELETE FROM 
  cd.bookings;

-- Query 6: Delete membber 37 who never made a booking
DELETE FROM 
  cd.members 
WHERE 
  memid = 37;

-- Query 7: retrieving facilities that charge less than 1/50 of the monthly maintenance cost
SELECT 
  facid, 
  name, 
  membercost, 
  monthlymaintenance 
FROM 
  cd.facilities 
WHERE 
  membercost > 0 
  AND membercost < monthlymaintenance / 50;

-- Query 8: Retrieve list of all facilities with the word "tennis" in the name'
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name LIKE '%Tennis%';

-- Query 9: Retrieve list of all facilities with id 1 and 5 without OR operator
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  facid IN (1, 5);

-- Query 10: Retrieve list of all members who joined after 2012-09-01
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.member 
where 
  joindate >= '2012-09-01'

-- Query 11: combine list of all surnames and all facility names union
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM 
  cd.facilities;

-- Query 12: retireve start times for bookings by members named 'David Farrell
SELECT 
  starttime 
FROM 
  cd.bookings 
  JOIN cd.members ON cd.members.memid = cd.bookings.memid 
WHERE 
  surname = 'Farrell' 
  AND firstname = 'David';

-- Query 13: list of all bookings for tennis courts on 2012-09-21
SELECT 
  starttime, 
  name 
FROM 
  cd.bookings 
  LEFT JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid 
WHERE 
  name IN (
    'Tennis Court 1', 'Tennis Court 2'
  ) 
  AND DATE(starttime) = '2012-09-21' 
ORDER BY 
  starttime;

-- Query 14: list of all members including the individual who recommended them
SELECT 
  m1.firstname, 
  m1.surname, 
  m2.firstname AS recommender_firstname, 
  m2.surname AS recommender_surname 
FROM 
  cd.members m1 
  LEFT JOIN cd.members m2 ON m1.recommendedby = m2.memid 
ORDER BY 
  m1.surname, 
  m1.firstname;

-- Query 15: list of all members who have recommended another member
SELECT 
  DISTINCT m2.firstname, 
  m2.surname 
FROM 
  cd.members m1 
  JOIN cd.members m2 ON m1.recommendedby = m2.memid 
ORDER BY 
  m2.surname, 
  m2.firstname;

-- Query 16: list of all members and their recommender
SELECT 
  DISTINCT m1.firstname || ' ' || m1.surname AS member, 
  (
    SELECT 
      m2.firstname || ' ' || m2.surname 
    FROM 
      cd.members m2 
    WHERE 
      m2.memid = m1.recommendedby
  ) AS recommender 
FROM 
  cd.members m1 
ORDER BY 
  member;

-- Query 17: Count the number of recommendations each member makes.
SELECT 
  recommendedby, 
  COUNT(*) 
FROM 
  cd.members 
WHERE 
  recommendedby IS NOT NULL 
GROUP BY 
  recommendedby 
ORDER BY 
  recommendedby;

-- Query 18: List the total slots booked per facility
SELECT 
  facid, 
  SUM(slots) AS total_slots 
FROM 
  cd.bookings 
GROUP BY 
  facid 
ORDER BY 
  facid;

-- Query 19: List the total slots booked per facility in a given month
SELECT 
  facid, 
  SUM(slots) AS total_slots 
FROM 
  cd.bookings 
WHERE 
  starttime >= '2012-09-01' 
  AND starttime < '2012-10-01' 
GROUP BY 
  facid 
ORDER BY 
  SUM(slots);

-- Query 20: List the total slots booked per facility per month
SELECT 
  facid, 
  EXTRACT(
    MONTH 
    FROM 
      starttime
  ) AS month, 
  SUM(slots) AS total_slots 
FROM 
  cd.bookings 
WHERE 
  starttime >= '2012-01-01' 
  AND starttime < '2013-01-01' 
GROUP BY 
  facid, 
  EXTRACT(
    MONTH 
    FROM 
      starttime
  ) 
ORDER BY 
  facid, 
  month;

-- Query 21: Find the count of members who have made at least one booking
SELECT 
  COUNT(DISTINCT memid) AS count 
FROM 
  cd.bookings;

-- Query 22: List each member's first booking after September 1st 2012
SELECT 
  DISTINCT m1.surname, 
  m1.firstname, 
  m1.memid, 
  min(b1.starttime) 
FROM 
  cd.members m1 
  JOIN cd.bookings b1 ON m1.memid = b1.memid 
WHERE 
  b1.starttime >= '2012-09-01' 
GROUP BY 
  m1.memid 
ORDER BY 
  m1.memid;

-- Query 23: Produce a list of member names, with each row containing the total member count
SELECT 
  COUNT(*) over(), 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;

-- Query 24: Produce a numbered list of members
SELECT 
  ROW_NUMBER() OVER(
    ORDER BY 
      joindate
  ) AS row_num, 
  firstname, 
  surname 
FROM 
  cd.members 
ORDER BY 
  joindate;

-- Query 25: Output the facility id that has the highest number of slots booked, again
SELECT 
  facid, 
  total_slots 
FROM 
  (
    SELECT 
      facid, 
      SUM(slots) AS total_slots 
    FROM 
      cd.bookings 
    GROUP BY 
      facid
  ) AS subquery 
ORDER BY 
  total_slots DESC 
LIMIT 
  1;

-- Query 26: Format the names of members
SELECT 
  CONCAT(surname, ', ', firstname) AS name 
FROM 
  cd.members;

-- Query 27: Find telephone numbers with parentheses
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone LIKE '%(%)%'

-- Query 28: Count the number of members whose surname starts with each letter of the alphabet
SELECT 
  SUBSTRING(surname, 1, 1) AS first_letter, 
  COUNT(*) AS count 
FROM 
  cd.members 
GROUP BY 
  first_letter 
ORDER BY 
  first_letter;
