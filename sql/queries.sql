\c exercises
-- Modifying Data
-- Q1
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);

--Q2
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT MAX(facid)+1, 'Spa', 20, 30, 100000, 800
FROM cd.facilities;

--Q3
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';

--Q4
UPDATE cd.facilities
SET membercost = (SELECT membercost * 1.1 FROM cd.facilities WHERE name = 'Tennis Court 1'),
    guestcost = (SELECT guestcost * 1.1 FROM cd.facilities WHERE name = 'Tennis Court 1')
WHERE name = 'Tennis Court 2';

--Q5
DELETE FROM cd.bookings;

--Q6
DELETE FROM cd.members
WHERE memid = 37;

--Basics
--Q1
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 AND (membercost * 50) < monthlymaintenance;

--Q2
SELECT * FROM cd.facilities
WHERE name LIKE '%Tennis%';

--Q3
SELECT * FROM cd.facilities
WHERE facid IN (1, 5);

--Q4
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01 00:00:00';

--Q5
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;

--Join
--Q1
SELECT starttime FROM cd.bookings
INNER JOIN cd.members
    ON cd.bookings.memid = cd.members.memid
WHERE firstname = 'David' AND surname = 'Farrell';

--Q2
SELECT starttime, name FROM cd.bookings
INNER JOIN cd.facilities
    ON cd.bookings.facid = cd.facilities.facid
WHERE
    name LIKE 'Tennis Court _' AND
    starttime BETWEEN '2012-09-21 00:00:00'
        AND '2012-09-21 23:59:59';

--Q3
SELECT
    mems.firstname AS memfname,
    mems.surname AS memsname,
    recs.firstname AS recfname,
    recs.surname AS recsname
FROM cd.members mems
         LEFT OUTER JOIN cd.members recs
                         ON mems.recommendedby = recs.memid
ORDER BY memsname, memfname ASC;

--Q4
SELECT DISTINCT recs.firstname, recs.surname
FROM cd.members mems
         INNER JOIN cd.members recs
                    ON mems.recommendedby = recs.memid
ORDER BY recs.surname, recs.firstname ASC;

--Q5
SELECT DISTINCT
    mems.firstname || ' ' || mems.surname AS member,
    (SELECT firstname || ' ' || surname AS recommender
     FROM cd.members recs
     WHERE recs.memid = mems.recommendedby)
FROM cd.members mems
ORDER BY member;

--Aggregation
--Q1
SELECT recommendedby, COUNT(recommendedby)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby ASC;

--Q2
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

--Q3
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime BETWEEN '2012-09-01 00:00:00' AND '2012-09-30 23:59:59'
GROUP BY facid
ORDER BY "Total Slots" ASC;

--Q4
SELECT
    facid,
    EXTRACT(MONTH FROM starttime) AS month,
    SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime BETWEEN '2012-01-01 00:00:00' AND '2012-12-31 23:59:59'
GROUP BY facid, month
ORDER BY facid, month ASC;

--Q5
SELECT COUNT(DISTINCT memid) FROM cd.bookings;

--Q6
SELECT surname, firstname, bks.memid, MIN(starttime) AS starttime
FROM cd.members mems
         INNER JOIN cd.bookings bks
                    ON mems.memid = bks.memid
WHERE starttime >= '2012-09-01'
GROUP BY surname, firstname, bks.memid
ORDER BY bks.memid;

--Q7
SELECT
    COUNT(*) OVER (),
    firstname,
    surname
FROM cd.members
ORDER BY joindate ASC;

--Q8
SELECT
    ROW_NUMBER() OVER (),
    firstname,
    surname
FROM cd.members
ORDER BY joindate ASC;

--Q9
SELECT facid, SUM(slots) AS total
FROM cd.bookings
GROUP BY facid
HAVING SUM(slots) = (SELECT MAX(total)
                     FROM (SELECT facid, SUM(slots) AS total
                           FROM cd.bookings
                           GROUP BY facid)
                              AS sunbquery);

--String
--Q1
SELECT (surname || ', ' || firstname) AS name FROM cd.members;

--Q2
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '(___)%';

--Q3
SELECT DISTINCT
    SUBSTRING(surname, 1, 1) AS letter,
    COUNT(surname) OVER (PARTITION BY SUBSTRING(surname, 1, 1))
FROM cd.members
ORDER BY letter ASC;