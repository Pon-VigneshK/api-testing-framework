package org.gcit.pojo;

import java.util.Collections;
import java.util.List;

public class Patient {
    private String resourceType;
    private String id;  // Keep id without hard coding
    private Meta meta = new Meta();  // Initialize meta to an instance with default values
    private List<Extension> extension = Collections.emptyList();  // Initialize extension to an empty list
    private List<Identifier> identifier = Collections.emptyList();  // Initialize identifier to an empty list
    private boolean active = false;  // Set default value
    private List<Name> name = Collections.singletonList(new Name());  // Initialize name to a list with a Name instance with default values
    private List<Telecom> telecom = Collections.emptyList();  // Initialize telecom to an empty list
    private String gender;  // Keep gender without hard coding
    private String birthDate;  // Keep birthDate without hard coding
    private boolean deceasedBoolean = false;  // Set default value
    private List<Address> address = Collections.emptyList();  // Initialize address to an empty list
    private MaritalStatus maritalStatus = new MaritalStatus();  // Initialize maritalStatus to an instance with default values
    private List<Communication> communication = Collections.emptyList();  // Initialize communication to an empty list

    // getters and setters

    // Inner classes representing nested structures
    public static class Meta {
        private String lastUpdated;
        // getters and setters
    }

    public static class Extension {
        private String url;
        private List<ExtensionItem> extension;
        // getters and setters
    }

    public static class ExtensionItem {
        private String url;
        private ValueCoding valueCoding;
        private String valueString;
        // getters and setters
    }

    public static class ValueCoding {
        private String system;
        private String code;
        private String display;
        // getters and setters
    }

    public static class Identifier {
        private String system;
        private String value;
        // getters and setters
    }

    public static class Name {
        private String family = "MITHON";  // Set default value for family
        private List<String> given = Collections.singletonList("JOHNSON");  // Initialize given to a list with a default value
        private List<String> suffix = Collections.singletonList("D");  // Initialize suffix to a list with a default value
        // getters and setters
    }

    public static class Telecom {
        private String system;
        private String value;
        private String use;
        // getters and setters
    }

    public static class Address {
        private String use;
        private String type;
        private String text;
        private List<String> line;
        private String city;
        private String state;
        private String postalCode;
        private Period period = new Period();  // Initialize period to an instance with default values
        // getters and setters
    }

    public static class Period {
        private List<ExtensionItem> extension = Collections.emptyList();  // Initialize extension to an empty list
        // getters and setters
    }

    public static class MaritalStatus {
        private List<Coding> coding = Collections.singletonList(new Coding());  // Initialize coding to a list with a Coding instance with default values
        // getters and setters
    }

    public static class Coding {
        private String system = "http://terminology.hl7.org/CodeSystem/v3-NullFlavor";  // Set default value for system
        private String code = "UNK";  // Set default value for code
        private String display = "UNKNOWN";  // Set default value for display
        // getters and setters
    }

    public static class Communication {
        private Language language = new Language();  // Initialize language to an instance with default values
        // getters and setters
    }

    public static class Language {
        private List<Coding> coding = Collections.singletonList(new Coding());  // Initialize coding to a list with a Coding instance with default values
        // getters and setters
    }
}
