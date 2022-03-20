drop table if exists account;
drop table if exists course; 
drop table if exists schedule;

create table if not exists course (
	CourseID int not null,
	CourseCode varchar(30) not null,
    CourseName varchar(255) not null,
    Weekday varchar(10),
    StartTime varchar(30),
    EndTime varchar(30),
    Enrollment int,
    Capacity int,
    constraint pk_course primary key(CourseID)
	);

create table if not exists schedule (
	ScheduleID varchar(30) not null,
    IsCurrent bit not null,
    CourseID int,
    constraint pk_schedule primary key (ScheduleID),
    constraint fk_schedule foreign key (CourseID) references course(CourseID)
	);
        
create table if not exists account (
    UserEmail varchar(255) not null,
    UserPassword varchar(255) not null,
    ScheduleID varchar(30),
    constraint pk_account primary key(UserEmail),
    constraint fk_account foreign key (ScheduleID) references schedule(ScheduleID)
	);

select * from course;

select * from account;
    


