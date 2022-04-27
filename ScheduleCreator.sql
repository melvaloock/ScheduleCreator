-- drop table if exists course;
-- drop table if exists recommendedCourse;
-- drop table if exists recommendedSchedule;
-- drop table if exists activity;
-- drop table if exists courseReference;
-- drop table if exists schedule;
-- drop table if exists account;

create table if not exists account (
    UserEmail varchar(255) not null,
    UserPassword varchar(255) not null,
    Major varchar(255),
    GradYear int,
    constraint pk_account primary key(UserEmail)
	);

create table if not exists schedule (
	ScheduleID varchar(30) not null,
    IsCurrent bit not null,
    UserEmail varchar(255) not null,
    constraint pk_schedule primary key (ScheduleID, UserEmail),
    constraint fk_schedule foreign key (UserEmail) references account(UserEmail)
	);
    
create table if not exists courseReference (
-- 	CourseCode varchar(30) not null,
--  CourseName varchar(255) not null,
	CourseID int not null,
    ScheduleID varchar(30) not null,
    UserEmail varchar(255) not null,
    constraint pk_courseReference primary key (CourseID, UserEmail),
    constraint fk_courseReference foreign key (ScheduleID, UserEmail) references schedule(ScheduleID, UserEmail)
	);
    
create table if not exists recommendedSchedule (
	Major varchar(50) not null,
    GradYear int not null,
    Semester varchar(30) not null,
    constraint pk_recommendedSchedule primary key (Major, GradYear, Semester)
	);
    
create table if not exists recommendedCourse (
	Major varchar(50) not null,
	Semester varchar(30) not null,
    GradYear int not null,
	CourseCode varchar(30) not null,
    CourseName varchar(255) not null,
    constraint pk_recommendedCourse primary key (Major, GradYear, Semester, CourseCode, CourseName), 
    constraint fk_recommendedCourse foreign key (Major, GradYear, Semester) references recommendedSchedule(Major, GradYear, Semester)
	);
    
create table if not exists course (
	CourseID int not null,
	CourseCode varchar(30) not null,
    CourseName varchar(255) not null,
    Weekday varchar(10),
    StartTime varchar(30),
    EndTime varchar(30),
    Enrollment int,
    Capacity int,
    constraint pk_course primary key (CourseID)
	);
    
create table if not exists activity (
	ActivityID int not null,
    ScheduleID varchar(30) not null,
    UserEmail varchar(255) not null,
    ActivityCode varchar(30) not null,
    ActivityName varchar(255) not null,
    Weekday varchar(10),
    StartTime varchar(30),
    EndTime varchar(30),
    constraint pk_activity primary key (ActivityID),
    constraint fk_activity foreign key (ScheduleID, UserEmail) references schedule(ScheduleID, UserEmail)    
	);
        
select * from course;

select * from account;

select * from schedule;

select * from courseReference;

select * from recommendedSchedule;

select * from recommendedCourse;

SELECT * FROM schedule WHERE UserEmail = "testy@test.com";

SELECT s.UserEmail, s.ScheduleID, s.IsCurrent, c.CourseID FROM schedule s inner join courseReference c ON (s.ScheduleID = c.ScheduleID and s.UserEmail = c.UserEmail) WHERE s.UserEmail = "testy@test.com";

-- insert into account values("hambykr19@gcc.edu", "somepasswordhash");

insert into schedule values("SPRING2022", 1, "hambykr19@gcc.edi");

select * from schedule where scheduleid = "SPRING2022" and useremail = "hambykr19@gcc.edu";

insert into courseReference values("courseCode", "courseName", "SPRING2022", "hambykr19@gcc.edu");

delete from courseReference where UserEmail = "hambykr19@gcc.edu" and CourseCode = "courseCode" and ScheduleID = "SPRING2022";

select * from course where coursecode like "%comp%";

-- for melva
select * from course where CourseName like "%music%";

insert into recommendedSchedule values("Computer Science (BS)", 2024, "Fall 2025");
insert into recommendedCourse values("Computer Science (BS)", "Fall 2025", 2024, "COMP 252  A", "COMPUTER ARCHITECTURE/ORG");
insert into recommendedCourse values("Computer Science (BS)", "Fall 2025", 2024, "COMP 222  A", "INTRO TO DATA STRUCT & ALGORITHMS");
insert into recommendedCourse values("Computer Science (BS)", "Fall 2025", 2024, "HUMA 301  A", "CIV/THE ARTS");

insert into recommendedSchedule values("Computer Science (BS)", 2024, "Fall 2026");
insert into recommendedCourse values("Computer Science (BS)", "Fall 2026", 2024, "COMP 252  A", "COMPUTER ARCHITECTURE/ORG");
insert into recommendedCourse values("Computer Science (BS)", "Fall 2026", 2024, "COMP 222  A", "INTRO TO DATA STRUCT & ALGORITHMS");
insert into recommendedCourse values("Computer Science (BS)", "Fall 2026", 2024, "HUMA 301  A", "CIV/THE ARTS");

insert into recommendedSchedule values("Computer Science (BA)", 2025, "Fall 2022");
insert into recommendedCourse values("Computer Science (BA)", "Fall 2022", 2025, "MATH 117  A", "FINITE MATH");
insert into recommendedCourse values("Computer Science (BA)", "Fall 2022", 2025, "COMP 222  A", "INTRO TO DATA STRUCT & ALGORITHMS");


select * from recommendedCourse where major = "Computer Science (BS)" and gradyear = 2023 and semester = "Fall 2022";

select * from course where coursecode = "COMP 222  A" and CourseName = "INTRO TO DATA STRUCT & ALGORITHMS";
select * from course where coursecode = "COMP 252  A" and CourseName = "COMPUTER ARCHITECTURE/ORG";
select * from course where coursecode = "HUMA 301  A" and CourseName = "CIV/THE ARTS";


