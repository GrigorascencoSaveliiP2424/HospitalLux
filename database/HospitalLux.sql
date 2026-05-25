CREATE DATABASE Uchet_Patientov
GO

USE Uchet_Patientov
GO

CREATE TYPE id_type FROM INT NOT NULL
GO

CREATE TYPE name_info FROM VARCHAR(50) NOT NULL
GO

CREATE TABLE  Department(
department_id id_type primary key check(department_id>999 and department_id <10000),
department_name name_info,
specialization name_info,
kol_ward INT NOT NULL
)
GO

CREATE TABLE Ward (
ward_id id_type primary key check(ward_id>999 and ward_id <10000),
number_of_ward INT NOT NULL,
type_ward name_info,
kol_seats INT NOT NULL,
department_id id_type foreign key references Department(department_id) check(department_id>999 and department_id <10000)
)
GO

CREATE TABLE Patient(
patient_id id_type primary key check(patient_id>999 and patient_id <10000),
patient_name name_info,
patient_adress name_info ,
patient_number CHAR(12) DEFAULT '+000000000000' NOT NULL,
patient_diagnosis name_info,
date_of_admission DATE NOT NULL,
ward_id id_type foreign key references Ward(ward_id) check(ward_id>999 and ward_id <10000)
)
GO

INSERT INTO Department (department_id, department_name, specialization, kol_ward)
VALUES
(1001, 'Cardiology', 'Heart diseases', 6),
(1002, 'Neurology', 'Brain and nervous system', 5),
(1003, 'Surgery', 'General surgery', 7),
(1004, 'Pediatrics', 'Child health', 6),
(1005, 'Oncology', 'Cancer treatment', 5),
(1006, 'Orthopedics', 'Bone and joint disorders', 5),
(1007, 'Dermatology', 'Skin diseases', 3),
(1008, 'Ophthalmology', 'Eye diseases', 3),
(1009, 'ENT', 'Ear Nose Throat', 3),
(1010, 'Therapy', 'Internal medicine', 8),
(1011, 'Urology', 'Urinary system', 3),
(1012, 'Gynecology', 'Women health', 4),
(1013, 'Endocrinology', 'Hormonal disorders', 3),
(1014, 'Gastroenterology', 'Digestive system', 4),
(1015, 'Pulmonology', 'Respiratory diseases', 4),
(1016, 'Infectious Diseases', 'Infections treatment', 4),
(1017, 'Emergency', 'Urgent medical care', 5),
(1018, 'Rehabilitation', 'Recovery treatment', 4),
(1019, 'Nephrology', 'Kidney diseases', 3),
(1020, 'Psychiatry', 'Mental health', 3);
GO

INSERT INTO Ward (ward_id, number_of_ward, type_ward, kol_seats, department_id)
VALUES
(2001, 101, 'Intensive Care', 2, 1001),
(2002, 102, 'Standard', 4, 1001),
(2003, 103, 'Standard', 4, 1001),
(2004, 104, 'VIP', 1, 1001),
(2005, 105, 'Standard', 5, 1001),
(2006, 106, 'Standard', 4, 1001),

(2007, 201, 'Standard', 4, 1002),
(2008, 202, 'Standard', 4, 1002),
(2009, 203, 'Intensive Care', 2, 1002),
(2010, 204, 'VIP', 1, 1002),
(2011, 205, 'Standard', 5, 1002),

(2012, 301, 'Post Surgery', 3, 1003),
(2013, 302, 'Post Surgery', 3, 1003),
(2014, 303, 'Standard', 4, 1003),
(2015, 304, 'Intensive Care', 2, 1003),
(2016, 305, 'VIP', 1, 1003),
(2017, 306, 'Standard', 5, 1003),
(2018, 307, 'Standard', 4, 1003),

(2019, 401, 'Children Ward', 6, 1004),
(2020, 402, 'Children Ward', 6, 1004),
(2021, 403, 'Standard', 4, 1004),
(2022, 404, 'VIP', 1, 1004),
(2023, 405, 'Standard', 5, 1004),
(2024, 406, 'Standard', 4, 1004),

(2025, 501, 'Chemotherapy', 4, 1005),
(2026, 502, 'Chemotherapy', 4, 1005),
(2027, 503, 'Standard', 3, 1005),
(2028, 504, 'VIP', 1, 1005),
(2029, 505, 'Standard', 4, 1005),

(2030, 601, 'Rehabilitation', 5, 1006),
(2031, 602, 'Standard', 4, 1006),
(2032, 603, 'Standard', 4, 1006),
(2033, 604, 'VIP', 1, 1006),
(2034, 605, 'Standard', 5, 1006),

(2035, 701, 'Skin Care', 3, 1007),
(2036, 702, 'Standard', 4, 1007),
(2037, 703, 'VIP', 1, 1007),

(2038, 801, 'Eye Care', 3, 1008),
(2039, 802, 'Standard', 4, 1008),
(2040, 803, 'VIP', 1, 1008),

(2041, 901, 'ENT Standard', 4, 1009),
(2042, 902, 'Standard', 4, 1009),
(2043, 903, 'VIP', 1, 1009),

(2044, 1001, 'General Ward', 6, 1010),
(2045, 1002, 'General Ward', 6, 1010),
(2046, 1003, 'Standard', 5, 1010),
(2047, 1004, 'Standard', 5, 1010),
(2048, 1005, 'VIP', 1, 1010),
(2049, 1006, 'Standard', 4, 1010),
(2050, 1007, 'Standard', 4, 1010),
(2051, 1008, 'Standard', 5, 1010),

(2052, 1101, 'Standard', 4, 1011),
(2053, 1102, 'VIP', 1, 1011),
(2054, 1103, 'Standard', 4, 1011),

(2055, 1201, 'Standard', 4, 1012),
(2056, 1202, 'Standard', 4, 1012),
(2057, 1203, 'VIP', 1, 1012),
(2058, 1204, 'Standard', 5, 1012),

(2059, 1301, 'Standard', 4, 1013),
(2060, 1302, 'Standard', 4, 1013),
(2061, 1401, 'Standard', 4, 1014),
(2062, 1402, 'Standard', 4, 1014),
(2063, 1403, 'VIP', 1, 1014),
(2064, 1404, 'Standard', 5, 1014),

(2065, 1501, 'Standard', 4, 1015),
(2066, 1502, 'Standard', 4, 1015),
(2067, 1503, 'Intensive Care', 2, 1015),
(2068, 1504, 'VIP', 1, 1015),

(2069, 1601, 'Standard', 4, 1016),
(2070, 1602, 'Standard', 4, 1016),
(2071, 1603, 'Isolation Ward', 2, 1016),
(2072, 1604, 'VIP', 1, 1016),

(2073, 1701, 'Emergency Ward', 5, 1017),
(2074, 1702, 'Emergency Ward', 5, 1017),
(2075, 1703, 'Intensive Care', 2, 1017),
(2076, 1704, 'Standard', 4, 1017),
(2077, 1705, 'VIP', 1, 1017),

(2078, 1801, 'Rehabilitation', 5, 1018),
(2079, 1802, 'Rehabilitation', 5, 1018),
(2080, 1803, 'Standard', 4, 1018),
(2081, 1804, 'VIP', 1, 1018),

(2082, 1901, 'Standard', 4, 1019),
(2083, 1902, 'Dialysis Ward', 3, 1019),
(2084, 1903, 'VIP', 1, 1019),

(2085, 2001, 'Standard', 4, 1020),
(2086, 2002, 'Psychiatry Ward', 3, 1020),
(2087, 2003, 'VIP', 1, 1020);
GO
GO

INSERT INTO Patient (patient_id, patient_name, patient_adress, patient_number, patient_diagnosis, date_of_admission, ward_id)
VALUES
(3001, 'Ivan Petrov', 'Chisinau', '+37360000001', 'Arrhythmia', '2026-02-01', 2001),
(3002, 'Maria Ivanova', 'Balti', '+37360000002', 'Migraine', '2026-02-03', 2007),
(3003, 'Andrei Popescu', 'Cahul', '+37360000003', 'Appendicitis', '2026-02-05', 2012),
(3004, 'Elena Sidorova', 'Orhei', '+37360000004', 'Flu', '2026-02-06', 2019),
(3005, 'Dmitri Moraru', 'Comrat', '+37360000005', 'Leukemia', '2026-02-07', 2025),
(3006, 'Natalia Rusu', 'Ungheni', '+37360000006', 'Fracture', '2026-02-08', 2030),
(3007, 'Sergey Lungu', 'Soroca', '+37360000007', 'Dermatitis', '2026-02-09', 2035),
(3008, 'Olga Balan', 'Hincesti', '+37360000008', 'Cataract', '2026-02-10', 2038),
(3009, 'Victor Ceban', 'Edinet', '+37360000009', 'Sinusitis', '2026-02-11', 2041),
(3010, 'Irina Munteanu', 'Chisinau', '+37360000010', 'Hypertension', '2026-02-12', 2044),
(3011, 'Alexei Romanov', 'Chisinau', '+37360000011', 'Asthma', '2026-02-13', 2045),
(3012, 'Ana Ciobanu', 'Balti', '+37360000012', 'Pneumonia', '2026-02-14', 2046),
(3013, 'Mihai Ursu', 'Orhei', '+37360000013', 'Diabetes', '2026-02-15', 2059),
(3014, 'Cristina Plamadeala', 'Cahul', '+37360000014', 'Gastritis', '2026-02-16', 2047),
(3015, 'Vasile Sandu', 'Comrat', '+37360000015', 'Bronchitis', '2026-02-17', 2049),
(3016, 'Sofia Rusu', 'Chisinau', '+37360000016', 'Kidney stones', '2026-02-18', 2052),
(3017, 'Ion Cazacu', 'Balti', '+37360000017', 'Tonsillitis', '2026-02-19', 2042),
(3018, 'Alina Grosu', 'Soroca', '+37360000018', 'Myopia', '2026-02-20', 2039),
(3019, 'Pavel Melnic', 'Ungheni', '+37360000019', 'Eczema', '2026-02-21', 2036),
(3020, 'Diana Rotaru', 'Chisinau', '+37360000020', 'Anemia', '2026-02-22', 2055),
(3021, 'Nicolai Stoian', 'Edinet', '+37360000021', 'Stroke', '2026-02-23', 2009),
(3022, 'Larisa Ceban', 'Hincesti', '+37360000022', 'Angina', '2026-02-24', 2002),
(3023, 'Petru Lungu', 'Orhei', '+37360000023', 'Hernia', '2026-02-25', 2013),
(3024, 'Valeria Botezatu', 'Cahul', '+37360000024', 'Otitis', '2026-02-26', 2041),
(3025, 'Denis Cojocaru', 'Balti', '+37360000025', 'Arthritis', '2026-02-27', 2031),
(3026, 'Tatiana Dima', 'Chisinau', '+37360000026', 'Hypertension', '2026-02-28', 2003),
(3027, 'Marin Brinza', 'Soroca', '+37360000027', 'Migraine', '2026-03-01', 2008),
(3028, 'Lilia Bucur', 'Comrat', '+37360000028', 'Flu', '2026-03-02', 2020),
(3029, 'Eugen Apostol', 'Ungheni', '+37360000029', 'Appendicitis', '2026-03-03', 2014),
(3030, 'Veronica Nistor', 'Chisinau', '+37360000030', 'Leukemia', '2026-03-04', 2026),
(3031, 'Oleg Rosca', 'Balti', '+37360000031', 'Fracture', '2026-03-05', 2032),
(3032, 'Gabriela Manole', 'Orhei', '+37360000032', 'Cataract', '2026-03-06', 2038),
(3033, 'Radu Cretu', 'Cahul', '+37360000033', 'Sinusitis', '2026-03-07', 2042),
(3034, 'Elina Mardari', 'Edinet', '+37360000034', 'Asthma', '2026-03-08', 2045),
(3035, 'Vlad Muntean', 'Chisinau', '+37360000035', 'Pneumonia', '2026-03-09', 2046),
(3036, 'Adriana Mocanu', 'Balti', '+37360000036', 'Diabetes', '2026-03-10', 2060),
(3037, 'Stefan Balan', 'Soroca', '+37360000037', 'Gastritis', '2026-03-11', 2047),
(3038, 'Nina Prisacaru', 'Hincesti', '+37360000038', 'Bronchitis', '2026-03-12', 2049),
(3039, 'Marius Toma', 'Comrat', '+37360000039', 'Kidney stones', '2026-03-13', 2052),
(3040, 'Victoria Popa', 'Chisinau', '+37360000040', 'Tonsillitis', '2026-03-14', 2043),
(3041, 'Daniel Enache', 'Balti', '+37360000041', 'Dermatitis', '2026-03-15', 2035),
(3042, 'Mihaela Spinu', 'Orhei', '+37360000042', 'Anemia', '2026-03-16', 2056),
(3043, 'Grigore Savin', 'Cahul', '+37360000043', 'Arrhythmia', '2026-03-17', 2004),
(3044, 'Doina Leanca', 'Soroca', '+37360000044', 'Migraine', '2026-03-18', 2007),
(3045, 'Alexandru Iovu', 'Ungheni', '+37360000045', 'Appendicitis', '2026-03-19', 2017),
(3046, 'Inga Frunza', 'Chisinau', '+37360000046', 'Flu', '2026-03-20', 2021),
(3047, 'Sergiu Ursachi', 'Edinet', '+37360000047', 'Leukemia', '2026-03-21', 2027),
(3048, 'Mariana Lupu', 'Balti', '+37360000048', 'Fracture', '2026-03-22', 2034),
(3049, 'Iurie Dobre', 'Orhei', '+37360000049', 'Eczema', '2026-03-23', 2036),
(3050, 'Rodica Gutu', 'Cahul', '+37360000050', 'Cataract', '2026-03-24', 2040),
(3051, 'Constantin Miron', 'Comrat', '+37360000051', 'Hypertension', '2026-03-25', 2044),
(3052, 'Angela Radu', 'Chisinau', '+37360000052', 'Asthma', '2026-03-26', 2045),
(3053, 'Boris Platon', 'Balti', '+37360000053', 'Pneumonia', '2026-03-27', 2046),
(3054, 'Ecaterina Croitoru', 'Soroca', '+37360000054', 'Diabetes', '2026-03-28', 2059),
(3055, 'Nicolae Arapu', 'Ungheni', '+37360000055', 'Gastritis', '2026-03-29', 2047),
(3056, 'Irina Tabac', 'Orhei', '+37360000056', 'Bronchitis', '2026-03-30', 2049),
(3057, 'Vadim Cucu', 'Cahul', '+37360000057', 'Kidney stones', '2026-03-31', 2054),
(3058, 'Olesea Roman', 'Chisinau', '+37360000058', 'Tonsillitis', '2026-04-01', 2042),
(3059, 'Victor Grama', 'Balti', '+37360000059', 'Myopia', '2026-04-02', 2039),
(3060, 'Natalia Macari', 'Edinet', '+37360000060', 'Anemia', '2026-04-03', 2058),
(3061, 'Ionela Ciorba', 'Chisinau', '+37360000061', 'Arrhythmia', '2026-04-04', 2005),
(3062, 'Mihail Covali', 'Balti', '+37360000062', 'Stroke', '2026-04-05', 2009),
(3063, 'Tatiana Burlacu', 'Orhei', '+37360000063', 'Angina', '2026-04-06', 2002),
(3064, 'Sorin Rusu', 'Cahul', '+37360000064', 'Hernia', '2026-04-07', 2018),
(3065, 'Elena Pintea', 'Soroca', '+37360000065', 'Otitis', '2026-04-08', 2043),
(3066, 'Andrei Ceban', 'Ungheni', '+37360000066', 'Arthritis', '2026-04-09', 2030),
(3067, 'Corina Lungu', 'Chisinau', '+37360000067', 'Hypertension', '2026-04-10', 2006),
(3068, 'Valentin Ciobanu', 'Balti', '+37360000068', 'Migraine', '2026-04-11', 2008),
(3069, 'Ludmila Casian', 'Orhei', '+37360000069', 'Flu', '2026-04-12', 2023),
(3070, 'Dorin Mitu', 'Cahul', '+37360000070', 'Appendicitis', '2026-04-13', 2012),
(3071, 'Nadejda Botnari', 'Comrat', '+37360000071', 'Leukemia', '2026-04-14', 2029),
(3072, 'Aurel Ciobanu', 'Chisinau', '+37360000072', 'Fracture', '2026-04-15', 2031),
(3073, 'Silvia Rotari', 'Balti', '+37360000073', 'Dermatitis', '2026-04-16', 2037),
(3074, 'Roman Guzun', 'Edinet', '+37360000074', 'Cataract', '2026-04-17', 2040),
(3075, 'Evelina Balan', 'Soroca', '+37360000075', 'Sinusitis', '2026-04-18', 2041),
(3076, 'Maxim Moraru', 'Ungheni', '+37360000076', 'Asthma', '2026-04-19', 2050),
(3077, 'Aliona Darii', 'Chisinau', '+37360000077', 'Pneumonia', '2026-04-20', 2051),
(3078, 'Vladimir Sandu', 'Balti', '+37360000078', 'Diabetes', '2026-04-21', 2060),
(3079, 'Loredana Postolachi', 'Orhei', '+37360000079', 'Gastritis', '2026-04-22', 2048),
(3080, 'Igor Negru', 'Cahul', '+37360000080', 'Bronchitis', '2026-04-23', 2049);
GO

UPDATE Department
SET kol_ward = 7
WHERE department_id = 1001
GO

UPDATE Ward
SET type_ward = 'VIP'
WHERE ward_id = 2002
GO

UPDATE Patient
SET patient_diagnosis = 'Chronic Migraine'
WHERE patient_id = 1005
GO

DELETE FROM Patient
WHERE patient_id = 1002
GO

INSERT INTO Patient
VALUES
(1002 ,'Alexei Romanov', 'Chisinau', '+37360000011', 'Asthma', '2026-02-20', 2002)
GO

CREATE INDEX 
patient_name_indx ON Patient (patient_name)
GO


----------------------------------------------------------------[Запросы]------------------------------------------------------------------------------

SELECT patient_name FROM Patient
GO
--------Patient--------
SELECT * 
FROM Patient
GO

SELECT * 
FROM Patient
WHERE date_of_admission = '2026-02-05'
GO

SELECT patient_name 
FROM Patient
WHERE patient_diagnosis = 'Flu'
GO

SELECT * 
FROM Patient
WHERE patient_adress = 'Chisinau'
GO
--------Ward----------
SELECT * 
FROM Ward
GO

SELECT * 
FROM Ward
WHERE type_ward = 'VIP'
GO

IF NOT EXISTS (
    SELECT *
    FROM Ward
    WHERE ward_id NOT IN (
        SELECT ward_id
        FROM Patient
    )
)
    THROW 50001, 'Нет палат без пациентов', 1
ELSE
    SELECT *
    FROM Ward
    WHERE ward_id NOT IN (
        SELECT ward_id
        FROM Patient
    )
GO
------Department------
SELECT * 
FROM Department
GO

SELECT * 
FROM Department
WHERE specialization LIKE '%Heart%'
GO


------------------------------------------[Запросы на группировку информации, с использованием функций агрегации]----------------------------------------------------

----------Patient-----------
SELECT ward_id, COUNT(*) AS patient_count
FROM Patient
GROUP BY ward_id
GO

SELECT patient_diagnosis, COUNT(*) AS total
FROM Patient
GROUP BY patient_diagnosis
GO

SELECT COUNT(*) AS patient_total_count
FROM Patient
GO
----------Ward-----------
SELECT AVG(kol_seats) AS avg_seats
FROM Ward
GO
----------Department-----------
SELECT COUNT(*) AS count_departments
FROM Department
GO

------------------------------------------------------------------------[Представления]-----------------------------------------------------------------
CREATE VIEW patient_ward AS
SELECT patient_id,patient_name, patient_number, patient_adress, patient_diagnosis, 
date_of_admission, number_of_ward, type_ward, kol_seats, department_id
FROM Patient INNER JOIN Ward ON Patient.ward_id = Ward.ward_id
GO

SELECT *
FROM patient_ward
GO

CREATE VIEW patient_department AS
SELECT patient_id, patient_name, patient_number, patient_adress, patient_diagnosis, 
date_of_admission, number_of_ward, type_ward, kol_seats, department_name, specialization
FROM patient_ward INNER JOIN Department ON patient_ward.department_id=Department.department_id
GO

SELECT *
FROM patient_department
GO

---------Запросы по Представлениям-----------

SELECT patient_id, patient_name, department_name
FROM patient_department
WHERE department_name = 'Cardiology'
GO

SELECT patient_id,patient_name, type_ward
FROM patient_ward
WHERE type_ward = 'VIP'
GO

SELECT department_name, COUNT(*) AS total
FROM patient_department
GROUP BY department_name
GO
-------------------------------------------------------------[Транзакции]-------------------------------------------------------------------------

----------Добавление пациента с проверкой мест--------------
BEGIN TRY
    BEGIN TRANSACTION

    IF (
        SELECT COUNT(*)
        FROM Patient
        WHERE ward_id = 2002
    ) >= (
        SELECT kol_seats
        FROM Ward
        WHERE ward_id = 2002
    )
    BEGIN
        THROW 50001, 'В палате нет свободных мест', 1
    END


    INSERT INTO Patient (patient_id,patient_name,patient_adress,patient_number,patient_diagnosis,date_of_admission,ward_id)
    VALUES (1111, 'New Patient','Chisinau','+37360000099','Flu',GETDATE(),2002)

    COMMIT
END TRY
BEGIN CATCH
    ROLLBACK
    PRINT ERROR_MESSAGE()
END CATCH
GO
----------Перевод пациента в другую палату с проверкой мест--------------
BEGIN TRY
    BEGIN TRANSACTION

    -- Проверка: существует ли пациент
    IF NOT EXISTS (
        SELECT 1 
        FROM Patient 
        WHERE patient_id = 1010
    )
        THROW 50001, 'Пациент не найден', 1

    -- Проверка: есть ли места в палате
    IF (
        SELECT COUNT(*) 
        FROM Patient 
        WHERE ward_id = 2003
    ) >= (
        SELECT kol_seats 
        FROM Ward 
        WHERE ward_id = 2003
    )
        THROW 50002, 'В палате нет свободных мест', 1

    -- Перевод пациента
    UPDATE Patient
    SET ward_id = 2003
    WHERE patient_id = 1010

    COMMIT
END TRY
BEGIN CATCH
    ROLLBACK
    PRINT ERROR_MESSAGE()
END CATCH
GO
----------Выписка пациента (удаление)--------------
BEGIN TRY
    BEGIN TRANSACTION

    IF EXISTS (
        SELECT 1 
        FROM Patient 
        WHERE patient_id = 1111
    )
    BEGIN
        DELETE FROM Patient
        WHERE patient_id = 1111
    END
    ELSE
        THROW 50002, 'Пациент не найден', 1

    COMMIT
END TRY
BEGIN CATCH
    ROLLBACK
    PRINT ERROR_MESSAGE()
END CATCH
GO
----------Добавление отделения и палаты--------------
BEGIN TRY
    BEGIN TRANSACTION

    INSERT INTO Department (department_id, department_name, specialization, kol_ward)
    VALUES (1011, 'Urology', 'Urinary system', 1)

    INSERT INTO Ward (ward_id, number_of_ward, type_ward, kol_seats, department_id)
    VALUES (2011, 11, 'Standard', 4, 1011)

    COMMIT
END TRY
BEGIN CATCH
    ROLLBACK
    PRINT ERROR_MESSAGE()
END CATCH
GO
-------------------------------------------------------------------[Роли]---------------------------------------------------------------------------

----------Добавление роли Доктора--------------
CREATE ROLE DoctorRole
GO

CREATE LOGIN DoctorLogin 
WITH PASSWORD = 'Doctor1234',
DEFAULT_DATABASE = Uchet_Patientov
GO

CREATE USER DoctorUser FOR LOGIN DoctorLogin
GO

ALTER ROLE DoctorRole ADD MEMBER DoctorUser
GO

GRANT SELECT ON Patient TO DoctorRole
GO

GRANT SELECT ON Ward TO DoctorRole
GO

GRANT SELECT ON Department TO DoctorRole
GO

GRANT UPDATE ON Patient TO DoctorRole
GO

----------Добавление роли Админа--------------
CREATE ROLE AdminRole
GO

CREATE LOGIN AdminLogin 
WITH PASSWORD = 'Admin1234',
DEFAULT_DATABASE = Uchet_Patientov
GO

CREATE USER AdminUser FOR LOGIN AdminLogin
GO

ALTER ROLE AdminRole ADD MEMBER AdminUser
GO

GRANT SELECT, INSERT, UPDATE, DELETE ON Patient TO AdminRole
GO

GRANT SELECT, INSERT, UPDATE, DELETE ON Ward TO AdminRole
GO

GRANT SELECT, INSERT, UPDATE, DELETE ON Department TO AdminRole
GO

----------Добавление роли Медсестра--------------
CREATE ROLE NurseRole
GO

CREATE LOGIN NurseLogin 
WITH PASSWORD = 'Nurse1234',
DEFAULT_DATABASE = Uchet_Patientov
GO

CREATE USER NurseUser FOR LOGIN NurseLogin
GO

ALTER ROLE NurseRole ADD MEMBER NurseUser
GO

GRANT SELECT ON patient_department TO NurseRole
GO
