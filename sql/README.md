# Introduction

# SQL Quries

###### Table Setup (DDL)

```bash
create table if not exists cd.members (
	memid INTEGER primary key,
	surname VARCHAR(200),
	firstname VARCHAR(200),
	address VARCHAR(300),
	zipcode INTEGER,
	telephone VARCHAR(20),
	recommendedby INTEGER,
	joindate TIMESTAMP,
	CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby)
		REFERENCES cd.members(memid) 
);

create table if not exists cd.facilities (
	facid INTEGER primary key,
	name VARCHAR(100),
	membercost numeric,
	guestcost numeric,
	initialoutlay numeric,
	monthlymaintenance numeric
);

create table if not exists cd.bookings (
	bookid INTEGER primary key,
	facid INTEGER,
	memid INTEGER,
	starttime TIMESTAMP,
	slots INTEGER,
	CONSTRAINT fk_bookings_facid FOREIGN KEY (facid)
		REFERENCES cd.facilities(facid),
	CONSTRAINT fk_bookings_memid FOREIGN KEY (memid)
		REFERENCES cd.members(memid)
);
```

###### Question 1: Modifying Data 

1a. 
```bash
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);
```
1b.
```bash
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT MAX(facid)+1, 'Spa', 20, 30, 100000, 800
FROM cd.facilities;
```
1c.
```bash
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';
```
1d.
```bash
UPDATE cd.facilities
SET membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE name = 'Tennis Court 1'), 
    guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE name = 'Tennis Court 1')
WHERE name = 'Tennis Court 2';
```
1e.
```bash
DELETE FROM cd.bookings;
```
1f.
```bash
DELETE FROM cd.members
WHERE memid = 37;
```

###### Questions 2: Basics

2a.
```bash
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 AND (membercost * 50) < monthlymaintenance;
```
2b.
```bash
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';
```
2c.
```bash
SELECT * FROM cd.facilities
WHERE facid IN (1, 5);
```
2d.
```bash
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01 00:00:00';
```
2e.
```bash
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;
```

###### Questions 3: Join

3a.
```bash
SELECT starttime FROM cd.bookings
INNER JOIN cd.members
	ON cd.bookings.memid = cd.members.memid
WHERE firstname = 'David' AND surname = 'Farrell';
```
3b.
```bash
SELECT starttime, name FROM cd.bookings
INNER JOIN cd.facilities
	ON cd.bookings.facid = cd.facilities.facid
WHERE 
	name LIKE 'Tennis Court _' AND
	starttime BETWEEN '2012-09-21 00:00:00' 
	AND '2012-09-21 23:59:59';
```
3c.
```bash
SELECT 
	mems.firstname AS memfname,
	mems.surname AS memsname,
	recs.firstname AS recfname,
	recs.surname AS recsname
FROM cd.members mems
LEFT OUTER JOIN cd.members recs
	ON mems.recommendedby = recs.memid
ORDER BY memsname, memfname ASC;
```
3d.
```bash
SELECT DISTINCT recs.firstname, recs.surname 
FROM cd.members mems
INNER JOIN cd.members recs
	ON mems.recommendedby = recs.memid
ORDER BY recs.surname, recs.firstname ASC;
```
3e.
```bash
SELECT DISTINCT
	mems.firstname || ' ' || mems.surname AS member,
	(SELECT firstname || ' ' || surname AS recommender 
	 FROM cd.members recs
	 WHERE recs.memid = mems.recommendedby) 
FROM cd.members mems
ORDER BY member;
```

###### Question 4: Aggregation

4a.
```bash
SELECT recommendedby, COUNT(recommendedby)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby ASC;
```
4b.
```bash
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
```
4c.
```bash
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime BETWEEN '2012-09-01 00:00:00' AND '2012-09-30 23:59:59'
GROUP BY facid
ORDER BY "Total Slots" ASC;
```
4d.
```bash
SELECT 
	facid,
	EXTRACT(MONTH FROM starttime) AS month,
	SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime BETWEEN '2012-01-01 00:00:00' AND '2012-12-31 23:59:59'
GROUP BY facid, month
ORDER BY facid, month ASC;
```
4e.
```bash
SELECT COUNT(DISTINCT memid) FROM cd.bookings;
```
4f.
```bash
SELECT surname, firstname, bks.memid, MIN(starttime) AS starttime 
FROM cd.members mems
INNER JOIN cd.bookings bks
	ON mems.memid = bks.memid
WHERE starttime >= '2012-09-01'
GROUP BY surname, firstname, bks.memid
ORDER BY bks.memid;
```
4g.
```bash
SELECT 
	COUNT(*) OVER (), 
	firstname, 
	surname
FROM cd.members
ORDER BY joindate ASC;
```
4h.
```bash
SELECT 
	ROW_NUMBER() OVER (), 
	firstname, 
	surname
FROM cd.members
ORDER BY joindate ASC;
```
4i.
```bash
SELECT facid, SUM(slots) AS total 
FROM cd.bookings
GROUP BY facid
HAVING SUM(slots) = (SELECT MAX(total) 
					  FROM (SELECT facid, SUM(slots) AS total
						    FROM cd.bookings 
					 		GROUP BY facid)
					 		AS sunbquery);
```

###### Question 5: String

5a.
```bash
SELECT (surname || ', ' || firstname) AS name FROM cd.members;
```
5b.
```bash
SELECT memid, telephone 
FROM cd.members
WHERE telephone LIKE '(___)%';
```
5c.
```bash
SELECT DISTINCT
	SUBSTRING(surname, 1, 1) AS letter,
	COUNT(surname) OVER (PARTITION BY SUBSTRING(surname, 1, 1))
FROM cd.members
ORDER BY letter ASC;
```

