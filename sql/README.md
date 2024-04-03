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

###### Question 1: Show all members 



###### Questions 2: Lorem ipsum...



