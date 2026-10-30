# VIT Semester Result

**Assignment:** Design and develop a responsive website to prepare one semester result of VIT students using Spring Boot and MySQL/MariaDB. The user enters exactly four subjects with MSE (30%) and ESE (70%) marks.

## Technologies

- Java 21, Spring Boot, Maven Wrapper
- Spring MVC, Thymeleaf, Spring Data JPA and Bean Validation
- MariaDB/MySQL through XAMPP
- HTML and responsive CSS

## Project structure

- `controller` accepts HTTP requests and selects Thymeleaf views.
- `form` validates browser input without exposing persistence entities directly.
- `service` calculates and saves results in a transaction.
- `entity` models `Student`, `SemesterResult`, and `ResultSubject` database records.
- `repository` provides JPA database access.

## Database setup

1. Start MySQL/MariaDB in the XAMPP Control Panel.
2. Open phpMyAdmin at `http://localhost/phpmyadmin`.
3. Import `DATABASE_SETUP.sql` from this project.
4. The script creates the `vit_semester_result` database and all three tables.

The default local configuration in `src/main/resources/application.properties` uses `root` with a blank password, the usual XAMPP default. If your local root account has a password, set `spring.datasource.password` to that local password before running.

## Run

From the project folder in PowerShell:

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Open `http://localhost:8080`.

The Maven Wrapper downloads Maven and the Maven dependencies automatically on its first use; no global Maven installation or manually downloaded JAR files are required.

## Calculation

For each subject, the server calculates:

```text
Final Marks = (MSE × 0.30) + (ESE × 0.70)
```

The result contains four final marks, their total, and their average. Because each input mark is normalized to 100, the average is displayed as the Overall Percentage. Marks must be from 0 to 100, subject names must be unique without regard to capitalization, and one PRN may have only one result for a given semester. No grades, CGPA, or pass/fail rule is used.

Sample: MSE 50 and ESE 50 produces final marks of 50.00. For four final values of 100, 50, 70, and 30, the total is 250.00 and overall percentage is 62.50%.
