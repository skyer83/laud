### 用于 RoleDirective 判断 account 是否拥有指定的 roles
#sql("hasRole")
	select ar.accountId from account_role ar
	inner join role r on ar.roleId = r.id
	where ar.accountId = #para(accountId)
	and (
		#for (x : roleNameArray)
			#(!for.first ? "or" : "") r.name = #para(x)
		#end
	)
#end

