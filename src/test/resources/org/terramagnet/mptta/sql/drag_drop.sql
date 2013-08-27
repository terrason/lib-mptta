--------------------------------------------------------------------
-- @param drapId
-- @param dropId
select dra.lid as draplid,dra.rid as draprid
,dro.lid as droplid,dro.rid as droprid
,pre.lid as prelid,pre.rid as prerid
,nxt.lid as nextlid,nxt.rid as nextrid
from mptta dra,mptta dro
left join mptta pre on pre.rid=dro.lid-1
left join mptta nxt on nxt.lid=dro.rid+1
where dra.id=? and dro.id=?;

update mptta set lid=(lid-?)*-1 where lid between ? and ?;
update mptta set rid=(rid-?)*-1 where rid between ? and ?;
update mptta set lid=lid+? where lid between ? and ?;
update mptta set rid=rid+? where rid between ? and ?;
update mptta set lid=lid*-1 where lid<0;
update mptta set rid=rid*-1 where rid<0;