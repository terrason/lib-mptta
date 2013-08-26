----------------------------------------------------
-- @param index
-- @param name
update mptta set lid=lid+2 where lid > ?-1;
update mptta set rid=rid+2 where rid > ?-1;
insert into mptta (lid,rid,name)values(?,?+1,?);
-----------------------------------------------------
update mptta set lid=lid+2 where lid > 28-1;
update mptta set rid=rid+2 where rid > 28-1;
insert into mptta (lid,rid,name)values(28,28+1,'测试组D');
update mptta set lid=lid+2 where lid > 30-1;
update mptta set rid=rid+2 where rid > 30-1;
insert into mptta (lid,rid,name)values(30,30+1,'测试组E');
update mptta set lid=lid+2 where lid > 32-1;
update mptta set rid=rid+2 where rid > 32-1;
insert into mptta (lid,rid,name)values(32,32+1,'测试组F');