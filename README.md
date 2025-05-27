# this tool only support sqlite database
## support Multi-Language
## use javaFx build user interface

this is a sqlite manager application  
I write it by java Language  
If you have any question submit issue  
you can see current UI in screenshot folder  

future plan:  
1. support multi-language ok
2. show tables use tree view ok
3. add table ok
4. edit table ok
5. remove table ok
6. generate test data todo
7. table column add notes todo
8. edit table column ok
9. show table data with page ok
10. execute select sql ok
11. execute insert or update or delete sql ok
12. show views todo
13. show triggers todo
14. show indexes todo
15. save query todo
16. backup database todo
17. when table data is empty show empty table bug fix todo

# Good Luck for every one

# 未来还可以写一个接口测试工具，支持http协议 get,post，支持websocket协议，支持rsocket协议，支持tcp客户端，服务端，支持udp客户端，服务端，支持mqtt客户端，服务端。


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