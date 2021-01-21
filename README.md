ReferExpert work space.
Prerequisites :
Eclipse, MySQL local database, gmail account to enable emails.

Once you have mysql installed, please make sure to run below commands to create a database and provide access to it.
create Database referexpert;

CREATE USER 'referexpert'@'localhost' IDENTIFIED WITH mysql_native_password BY 'MySQL*987';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,REFERENCES,ALTER,INDEX on referexpert.* TO 'referexpert'@'localhost';
flush privileges;  

To run the application go to class ReferexpertApplication.java in Eclipse. Right click, Run As --> Java Application. Which will automatically create required tables along with static data.