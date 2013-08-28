----------------------------------------------------
-- @param index
-- @param name
update mptta set lid=lid+2 where lid > ?-1;
update mptta set rid=rid+2 where rid > ?-1;
insert into mptta (lid,rid,name)values(?,?+1,?);
