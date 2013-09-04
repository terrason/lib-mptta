select node.id,concat(repeat('    ', count(parent.name) - 1), node.name) as name,node.lid,node.rid
from tbl_enterprise_structure as node,
tbl_enterprise_structure as parent
where node.lid between parent.lid and parent.rid
group by node.id
order by node.lid;
