# **play-crud-scala**

## 1 - Description:

- Sample project made on Scala and Play Framework, with PostgreSQL database. <br> Preview: (https://play-crud-scala.herokuapp.com/)

## 2 - Features:

- Inclusion, Search and Edit and Delete operations made by login;
- User password with SHA512 hash;
- CSRF *(Cross-site Request Forger)* filter on forms;
- Allowed Hosts Filter;
- Redirect Https Filter;
- **Basic CORS *(Cross-origin resource sharing)* settings:**
	- Path Prefixes;
	- Allowed Origins;
	- Allowed Http Methods;
	- Allowed Http Headers;
	- Preflight Max Age;

## 3 - Libraries (build.sbt):

- **sbt.version**=1.2.8
- **Password Hash:**
"com.roundeights" %% "hasher" % "1.2.0"
- **Database Connection:**
"org.postgresql" % "postgresql" % "42.2.5"

## 4 - Plugins (plugin.sbt):

- **scalaVersion** := "2.12.8"
- **Heroku deploy:**
addSbtPlugin("com.heroku" % "sbt-heroku" % "2.1.2")

## 5 - Front-end:

- Bootstrap v3.3.7
- jQuery v2.1.1
- DataTables Bootstrap 3 v2.2.1
- DataTables v1.10.19
- jquery-maskmoney v3.1.1
- Datetimepicker for Bootstrap 3 v4.15.35
- moment.js v2.9.0
- Font Awesome 4.3.0
