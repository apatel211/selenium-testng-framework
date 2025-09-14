Selenium + TestNG UI Automation Framework (MultiBank)

How to use:
1. Import this folder as a Maven project in IntelliJ (File -> Open).
2. Edit src/test/resources/config.properties to set gridUrl if you want to run on Selenium Grid.
3. Run:
   mvn clean test -Dbrowser=chrome
   or for grid:
   mvn clean test -Dbrowser=chrome -DgridUrl=http://10.0.0.47:4444

Reports:
- Extent report: target/extent-report.html
- Screenshots: target/screenshots/

Notes:
- Locators may need adjustment if the site DOM changes.
- JsonUtils.readJson("filename.json") is reusable for any JSON file under src/test/resources.
