select id from tbl_enterprise_structure where pId=? order by sort;
update tbl_enterprise_structure set lid=?,rid=? where id=?;
update tbl_enterprise_structure d
left join (
    select node.id,count(distinct s.id) cnt from tbl_enterprise_structure node
    left join tbl_enterprise_structure desendant on desendant.lid between node.lid and node.rid
    left join tbl_enterprise_staff s on s.department_id=desendant.id
    group by node.id
) x on x.id=d.id
set d.staff_count=x.cnt;