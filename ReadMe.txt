PreRequisites that i used:
1. Java 1.8
2. Apache Maven 3.3.9
3. Selenium 3.14.0
4. testNG 6.14.3
5. Eclipse IDE


1) Import project into you IDE(ex: eclipse)
2) You can find 3 packages named `common`, `pages` and `test_scenarios`  in `src/test/java`
	`common` 			: This package containing `Common_methods.java` file which is holding some common methods that uses across the project
	`pages`  			: This package is to implement `Page Object Model`, containing two java classes named `Angel_jobs.java` & `Angel_profile_page.java` .
							`Angel_jobs.java`     : This java file contains the required elements of `JOBS` page, hence to use across the project
							`Angel_profile_page`  : This java file contains the required elements of `PROFILE` page, hence to use across the project
	`test_scenarios`	: This package holds the test scenarios, containing one class named `SearchJobsAndApply.java`
							`SearchJobsAndApply.java` : This is our main scenario file which containing 3 test cases names "user_skills", "searchJobsStoreInDB" and "applyJobWithSkillMatch", we have to run this file via `TestNG`
	 `user_skills` method takes skills that user having
	 `searchJobsStoreInDB` method search jobs with provided keywords and store data to DB
	 `applyJobWithSkillMatch` method takes data from DB and on required percentage match of skills, job will get applied.
3) `envProperties` folder : This folder having env.properties containing input data that used over the project
4) `inputs` : this folder having the file `input_keywords_search.txt` containing predefined search keywords
5) `screenshots` : Whenever failure comes a screenshot will be taken and stores here


How to run?
way 1:  Open `SearchJobsAndApply.java` class and run as testNG
way 2:  Right click on project and run as TestNG

Reports: 
1. After completion of running script, a decent report will be generated and stored in 	`SHC_Technologies_Task/test-output/STMExtentReport.html`. Open this file to see the HTML report.