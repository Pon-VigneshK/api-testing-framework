**Rest Assured + TestNG + Extent Report for API Automation**

**Preferred Java Version : JDK-11**

**Libraries Used**:
1. [Rest Assured for Api automation](https://github.com/rest-assured/rest-assured)
2. [Maven as build tool](https://maven.apache.org/guides/)
3. [TestNG as testing framework](https://testng.org/doc/)
4. [Extent for reporting](https://www.extentreports.com/docs/versions/4/java/index.html)
5. [Java Faker for random data](https://github.com/DiUS/java-faker)
6. [AssertJ Core for assert the test](https://assertj.github.io/doc/#assertj-core)
7. [Apache POI for reading Excel for test data](https://poi.apache.org/)


### Framework Structure and Rules:

**Package Structure:**
- `src/test/java`: Write regression suite and automation test cases.
- `src/main/java`: Keep non-testing content (e.g., utility classes, reporting, listening).
- Utilize a testng.xml file for running a suite of tests.

**Code Conventions:**
- Follow access level order: `private > protected > default > public`.
- Perform null checks on variables before operations.
- Prefer ENUM for predefined set of values.

### Data Provider:

**Data Provider Best Practices:**
- Use a single data provider method to handle all test methods.
- Read data from the source once and store it in a collection for reuse.
- Avoid tight coupling between Excel and Data Provider; support other file formats.
- Handle empty cells and type conversions appropriately.

### Static Blocks:

**Static Blocks:**
- Use static blocks for one-time initialization of static variables.
- Avoid unnecessary static block execution to save memory.
- Eager Initialization using static blocks.

### TestNG Listeners:

**TestNG Listeners:**
- Separate setup/teardown activities into listeners to decouple from tests.
- Implement commonly used listeners for setup, teardown, and reporting.
- Control annotations and customize test case execution using listeners.

### Extent Reports Integration:

**Extent Reports:**
- Implement Extent Reports using a dedicated class.
- Utilize ThreadLocal to handle thread safety.
- Restrict access to report methods using a separate class (ExtentLogger).
- Create a listener class for Extent Reports integration.


### Custom Annotations:

**Custom Annotations:**
- Use annotations for refactoring and enhancing code.
- Implement Reflections for annotation usage.
- Create custom annotations to replace hardcoded values.

### Miscellaneous:

**Retry Failed Tests:**
- Implement IRetryAnalyzer and AnnotationTransformer listeners.
- Retry failed tests judiciously; focus on fixing issues.

**Integration with Excel Sheet:**
- Use Excel sheets for test data.
- Integrate Data Provider with Excel for dynamic test data.

**Disabling Test Cases at Runtime:**
- Utilize MethodInterceptor Listener to disable certain test cases.


