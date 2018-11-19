# BaseDAO
This is a database connection tool, developer can use this tool to connect database from xml configure text, and this tool can easily to access database. and this tool also can excute sql, procedure and using object to access database.

it help developer to quickly access database and they don't use much time on the task.

Config.xml
this document configure database connection configuration, it includes database name, user name, password, port, connection type（direct or brigde）.

DBOperate.xml
this document set up factory implement object.

PojoConfig.xml
this document need to configure pojo class which must reflect to one database table.

example:
if you want to query a table which name pet, you can excute below:
Pet pet = new Pet();
xxx.find(pet);

then you can get all of data from database pet table.
