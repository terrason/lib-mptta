--------------------------------------------------------------------
-- @param id
select lid,rid,(rid - lid + 1)as width,(lid-?) as distance from mptta where id=?;
update mptta set lid=lid-?

---------------------------------------------------------------------
select node.id,concat( repeat('        ', count(parent.name) - 1), node.name) as name,node.lid,node.rid
from mptta as node,
mptta as parent
where node.lid between parent.lid and parent.rid
group by node.name
order by node.lid;
---------------------------------------------------------------------
select @slid := slid, @srid := srid, @width := width,@index:=index, @lid:=lid,@shift:=slid-dlid,@sign:=sign(slid-dlid) from(
    select s.lid as slid, s.rid as srid, s.rid - s.lid + 1 as width
    ,case 1 when 0 then d.rid when 1 then d.rid+1 when -1 then pre.lid end as index
    ,case 1 when 0 then rd.rld when 1 then rd.rid+1 when -1 then rd.lid end as dlid
    from mptta s,mptta d
    left join mptta pre on pre.rid=d.lid-1
    left join mptta nxt on nxt.lid=d.rid+1
    where s.id = 1000064 and d.id=1000073 and rd.rid=(if(s.lid>d.lid,d.rid,d.lid-1))
)x;
update mptta set lid=(lid-@shift)*-1,rid=(rid-@shift)*-1 where lid between @slid and @srid;
update mptta set lid=lid+@width*@sign where lid between if(@sign>0,@lid,@slid) and if(@sign>0,@slid,@lid);
update mptta set rid=rid+@width*@sign where rid between if(@sign>0,@lid,@slid) and if(@sign>0,@slid,@rdrid);
update mptta set lid=lid*-1,rid=rid*-1 where lid<0;

