# this tool only support sqlite database
## support Multi-Language
## use javaFx build user interface

this is a sqlite manager application  
I write it by java Language  
If you have any question submit issue  

# Good Luck for every one



SELECT name FROM sqlite_master WHERE type='table'

WITH all_tables AS (SELECT name FROM sqlite_master WHERE type = 'table')
SELECT at.name table_name, pti.*
FROM all_tables at INNER JOIN pragma_table_info(at.name) pti
ORDER BY table_name;


select * from pragma_table_info('users')

select * from pragma_table_info('userRoles')

SELECT *
FROM sqlite_master
WHERE type='table' AND sql LIKE '%FOREIGN KEY%';