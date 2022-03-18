-- drop table if exists account;
-- drop table if exists course; 
-- drop table if exists schedule;

create table if not exists account (
	UserID int not null,
    UserEmail varchar(255) not null,
    UserPassword varchar(255) not null,
    constraint pk_account primary key(UserID),
    constraint ck_account unique(UserEmail),
    constraint fk_account foreign key (ScheduleID) references schedule(ScheduleID)
	);

create table if not exists schedule (
	ScheduleID int not null,
    IsCurrent bit not null,
    constraint fk_schedule foreign key (CourseID) references course(CourseID)
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
    constraint pk_course primary key(CourseID)
	);

select * from course;
    


