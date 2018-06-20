### 用于 PermissionDirective 判断 account 是否拥有指定的 permission
#sql("hasPermission")
	select ar.accountId from (
		select rp.roleId from role_permission rp
		inner join permission p on rp.permissionId = p.id
		where p.actionKey = #para(actionKey)
	)
	as t inner join account_role ar on t.roleId = ar.roleId
	where ar.accountId = #para(accountId)
#end

