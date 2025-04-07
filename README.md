
# Play-CRUD-Scala  

![Programming Language](https://img.shields.io/badge/Scala-red?style=flat&logo=scala&logoColor=white)  
![Framework](https://img.shields.io/badge/Play!%20Framework-green?style=flat&logo=play&logoColor=white) ![Framework](https://img.shields.io/badge/Bootstrap-purple?style=flat&logo=bootstrap&logoColor=white) ![Library](https://img.shields.io/badge/jQuery-blue?style=flat&logo=jquery&logoColor=white)  
![Database](https://img.shields.io/badge/PostgreSQL-darkblue?logo=postgresql&logoColor=white)  
![Platform: Web](https://img.shields.io/badge/Platform-Web-blue?logo=google-chrome)
![Last Commit](https://img.shields.io/github/last-commit/ander1code/play-crud-scala?color=yellow&logo=github) ![Size](https://img.shields.io/github/repo-size/ander1code/play-crud-scala?color=blue&logo=files) ![License](https://img.shields.io/github/license/ander1code/play-crud-scala?color=black&logo=open-source-initiative)

---

## 1. Description
**Play-CRUD-Scala** is a sample project developed using **Scala** and the **Play Framework**, integrated with a **PostgreSQL database**. It showcases essential CRUD functionalities with a strong emphasis on security and cross-origin compatibility.

**Live Preview:** [Play-CRUD-Scala on Heroku](https://play-crud-scala.herokuapp.com/)

---

## 2. Features
- **CRUD Operations:** Includes Create, Read, Update, and Delete operations with user authentication.
- **User Authentication:**
  - Passwords are securely hashed using **SHA-512**.
- **Security Features:**
  - **CSRF Protection:** Cross-site request forgery filters on forms.
  - **Allowed Hosts Filter:** Limits requests to predefined hosts.
  - **Redirect HTTPS Filter:** Ensures requests are redirected to HTTPS.
- **CORS Settings:** Basic **Cross-Origin Resource Sharing** setup, including:
  - Path Prefixes
  - Allowed Origins
  - Allowed HTTP Methods
  - Allowed HTTP Headers
  - Preflight Max Age

---

## 3. Libraries (`build.sbt`)
- **SBT Version:** 1.2.8
- **Password Hashing:** 
  - `"com.roundeights" %% "hasher" % "1.2.0"`
- **Database Connection:** 
  - `"org.postgresql" % "postgresql" % "42.2.5"`

---

## 4. Plugins (`plugin.sbt`)
- **Scala Version:** 2.12.8
- **Heroku Deployment:** 
  - `addSbtPlugin("com.heroku" % "sbt-heroku" % "2.1.2")`

---

## 5. Front-End
- **Bootstrap:** v3.3.7 (for responsive design)
- **jQuery:** v2.1.1 (for interactivity)
- **DataTables Bootstrap:** v3 v2.2.1 (for tabular data styling)
- **DataTables:** v1.10.19 (advanced table functionality)
- **jquery-maskmoney:** v3.1.1 (input masking for monetary values)
- **Datetimepicker for Bootstrap:** v4.15.35 (date and time selection)
- **Moment.js:** v2.9.0 (date manipulation)
- **Font Awesome:** v4.3.0 (icon library)