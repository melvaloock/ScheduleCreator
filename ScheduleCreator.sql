-- drop table if exists course;
drop table if exists recommendedCourse;
drop table if exists recommendedSchedule;
drop table if exists courseReference;
drop table if exists schedule;
drop table if exists account;

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
    constraint pk_recommendedCourse primary key (Semester, CourseCode, CourseName), 
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
        
select * from course;

select * from account;

select * from schedule;

select * from courseReference;

insert into account values("hambykr19@gcc.edu", "somepasswordhash");

insert into schedule values("SPRING2022", 1, "hambykr19@gcc.edu");

insert into courseReference values("courseCode", "courseName", "SPRING2022", "hambykr19@gcc.edu");

delete from courseReference where UserEmail = "hambykr19@gcc.edu" and CourseCode = "courseCode" and ScheduleID = "SPRING2022";

-- for melva
select * from course where CourseName like "%music%";