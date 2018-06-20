### 超级管理员的 roleId 值为固定为 1
#sql("isSuperAdmin")
	select accountId from account_role
	where accountId = #para(0) and roleId = 1
#end

### 非超级管理员验证是否拥有具体权限
#sql("hasPermission")
	select ar.accountId from (
		select rp.roleId from role_permission rp
		inner join permission p on rp.permissionId = p.id
		where p.actionKey = #para(0)
	)
	as t inner join account_role ar on t.roleId = ar.roleId
	where ar.accountId = #para(1)
#end

