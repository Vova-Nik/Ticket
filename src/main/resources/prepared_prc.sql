create
or
replace function find_all(p_db_name IN varchar(20)) returns refcursor as
    $$
    declare
    db_cursor refcursor;
begin open db_cursor for
execute format('select * from %I', p_db_name);
return db_cursor;
end ;
$$ language plpgsql;