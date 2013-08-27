------------------------------------------------------------
-- @param id
select lid,rid,(rid - lid + 1)as width from mptta where id=?;
------------------------------------------------------------
-- @param lid 
-- @param rid
-- @param width
delete from mptta where lid between ? and ?;
update mptta set rid = rid - ? where rid > ?;
update mptta set lid = lid - ? where lid > ?;
------------------------------------------------------------
SELECT @myLeft := lid, @myRight := rid, @myWidth := rid - lid + 1 FROM mptta WHERE id = 1000068;
DELETE FROM mptta WHERE lid BETWEEN @myLeft AND @myRight;
UPDATE mptta SET rid = rid - @myWidth WHERE rid > @myRight;
UPDATE mptta SET lid = lid - @myWidth WHERE lid > @myRight;


delete from mptta where id>1000035;
------------------------------------------------------------