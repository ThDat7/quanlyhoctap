# University Management System

## Introduction

This system is designed to facilitate seamless management of academic activities for students, lecturers, staff and administrators. It streamlines various processes like course registration, schedule management, grade entry, and user administration. The platform ensures efficient communication and task handling for all users, contributing to a smooth academic experience.

- [University Management System](#university-management-system)
  - [Introduction](#introduction)
  - [Features](#features)
    - [Student](#student)
    - [Lecturer](#lecturer)
    - [Staff \& Admin](#staff--admin)
  - [Technologies Used](#technologies-used)
    - [Languages](#languages)
    - [Frameworks](#frameworks)
  - [Interface](#interface)
  - [Setup](#setup)
  - [About Frontend Repository](#about-frontend-repository)

## Features

### Student

Search and view course syllabus.
View curricula by faculty and academic year.
Register for courses.
View announcements.
Check timetables and exam schedules.
View academic results and tuition fees.
Verify student enrollment status.

### Lecturer

Create and edit course syllabus.
View teaching schedules.
Input grades.
Manage exam schedules.

### Staff & Admin

Manage courses and curricula.
Automatically & manually copy curricula and course syllabus from previous years.
Assign faculty for course syllabus preparation.
Manage announcements, departments, programs, and tuition fees.
Manage user accounts.
Automatically & manually create registration classes and schedules.
Manage student enrollment status.

## Technologies Used

### Languages

- Java
- JavaScript
- SQL

### Frameworks

- Spring MVC
- ReactJs ([Frontend repository](#about-client-repository))

## Interface

Register for courses
![Register for courses](https://res.cloudinary.com/drjfcdgoa/image/upload/v1727120417/bflu3wkcqkfk8y5xsmzj.png 'Courses register')

Some student features
![Some student features](https://res.cloudinary.com/drjfcdgoa/image/upload/v1727120417/u32zbe0vtoxzafgdkgbu.png 'Some student features')

Curricula view
![Curricula view feature](https://res.cloudinary.com/drjfcdgoa/image/upload/v1727120417/evr3ycwxrtdt6yyyfy30.png 'Curricula')

## Setup

1. Clone the repository:
   ```
   git clone https://github.com/ThDat7/quanlyhoctap.git
   ```
2. Navigate to the project directory:
   ```
   cd quanlyhoctap
   ```
3. Set up the database using the provided SQL scripts:
   Execute `DROP_CREATE_SCHEMA_SCRIPT.sql` to drop and create the schema.
4. Build the project using Maven:
   ```
   mvn clean install
   ```
5. Run the application:
   ```
   mvn spring-boot:run
   ```
6. Go to the generate data endpoint to fake data:
   `localhost/data`

## About Frontend Repository

[About that repository](https://github.com/ThDat7/quanlyhoctap-frontend)
