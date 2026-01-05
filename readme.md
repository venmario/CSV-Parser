## Features

- 🎯 **Declarative Schema Definition** - Use annotations to define your CSV structure
- ✅ **Built-in Validators** - `@NotBlank`, `@Email`, `@Min`, `@Max`, and more
- 🔧 **Custom Validators** - Implement business logic with `FieldValidator<T>`
- 🔄 **Type Conversion** - Automatic conversion to Integer, BigDecimal, LocalDate, etc.
- 🎛️ **Flexible Configuration** - Custom delimiters, encoding, validation modes
- 📊 **Error Collection** - Get all errors with row and field details
- 🌐 **Encoding Support** - Handle UTF-8, Windows-1252, and other encodings

## Installation

### Maven

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/venmario/Parsefy</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.mihok.parsefy</groupId>
        <artifactId>parsefy</artifactId>
        <version>1.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/venmario/Parsefy")
        mavenContent {
            snapshotsOnly()
        }
    }
}

dependencies {
    implementation "org.mihok.parsefy:parsefy:1.0.1-SNAPSHOT"
}

```

## Quick Start

### 1. Define Your Schema

```java
import org.mihok.parsefy.annotation.*;
import org.mihok.parsefy.validation.*;
import org.mihok.parsefy.core.*;

@CsvSchema
public class User {
    @CsvColumn(name = "user_id", required = true)
    @NotBlank(message = "User ID cannot be empty")
    private String userId;
    
    @CsvColumn(name = "email")
    @Email(message = "Invalid email format")
    private String email;
    
    @CsvColumn(name = "age")
    @Min(value = 18, message = "Must be 18 or older")
    @Max(value = 120, message = "Invalid age")
    private Integer age;
    
    @CsvColumn(name = "registration_date")
    @DateFormat("yyyy-MM-dd")
    private LocalDate registrationDate;
    
    // Getters and setters...
}
```

### 2. Parse Your CSV

```java
// Simple parsing - throws exception if errors occur
List<User> users = Parsefy.builder(User.class)
    .parse(new FileReader("users.csv"))
    .getResult()
    .getValidRows();

// Or get detailed results with error collection
ParsefyResult<User> result = Parsefy.builder(User.class)
    .parse(new FileReader("users.csv"))
    .getResult();

if (result.hasErrors()) {
    for (RowError error : result.getErrors()) {
        System.out.println(error); // Row 5, Field 'email': Invalid email format
    }
}

List<User> validUsers = result.getValidRows();
```

## Configuration

### Custom Delimiter

```java
List<User> users = Parsefy.builder(User.class)
    .delimiter(";")  // Use semicolon instead of comma
    .parse(reader)
    .getResult()
    .getValidRows();
```

### Character Encoding

```java
// Specify encoding explicitly
List<User> users = Parsefy.builder(User.class)
    .parse(inputStream, StandardCharsets.UTF_8)
    .getResult()
    .getValidRows();

// From file with Windows-1252 encoding
List<User> users = Parsefy.builder(User.class)
    .parse(new File("data.csv"), Charset.forName("Windows-1252"))
    .getResult()
    .getValidRows();
```

### Validation Modes

```java
// Strict mode (default) - stops on first error
List<User> users = Parsefy.builder(User.class)
    .strictMode(true)
    .parse(reader)
    .getResult()
    .getValidRows();

// Lenient mode - collects all errors
ParsefyResult<User> result = Parsefy.builder(User.class)
    .strictMode(false)
    .parse(reader)
    .getResult();
```

## Built-in Validators

| Annotation | Description | Example |
|------------|-------------|---------|
| `@NotBlank` | Ensures string is not null or empty | `@NotBlank(message = "Name required")` |
| `@Email` | Validates email format | `@Email(message = "Invalid email")` |
| `@Min` | Minimum numeric value | `@Min(value = 0, message = "Must be positive")` |
| `@Max` | Maximum numeric value | `@Max(value = 100, message = "Cannot exceed 100")` |
| `@DateFormat` | Date parsing pattern | `@DateFormat("yyyy-MM-dd")` |

## Custom Validators

### Simple Custom Validator

```java
public class PositiveNumberValidator implements FieldValidator<BigDecimal> {
    @Override
    public ValidationResult validate(BigDecimal value, ValidationContext context) {
        if (value != null && value.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.error("Value must be positive");
        }
        return ValidationResult.success();
    }
}

// Use it
@CsvColumn(name = "price")
@CustomValidator(PositiveNumberValidator.class)
private BigDecimal price;
```

## Type Conversion

Parsefy automatically converts string values to the appropriate Java types:

| Java Type | Example CSV Value | Notes |
|-----------|-------------------|-------|
| `String` | `John Doe` | No conversion |
| `Integer` | `42` | Parses integers |
| `Long` | `9223372036854775807` | Parses long values |
| `Double` | `3.14159` | Parses decimals |
| `BigDecimal` | `123.45` | Precise decimal handling |
| `Boolean` | `true`, `false` | Case-insensitive |
| `LocalDate` | `2024-01-15` | Requires `@DateFormat` |

### Custom Date Formats

```java
@CsvColumn(name = "birth_date")
@DateFormat("dd/MM/yyyy")  // European format
private LocalDate birthDate;

@CsvColumn(name = "created_at")
@DateFormat("yyyy-MM-dd HH:mm:ss")  // With time
private LocalDate createdAt;
```