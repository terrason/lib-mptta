select node.id,concat( repeat('        ', count(parent.name) - 1), node.name) as name,node.lid,node.rid
from mptta as node,
mptta as parent
where node.lid between parent.lid and parent.rid
group by node.name
order by node.lid;

select * from mptta;