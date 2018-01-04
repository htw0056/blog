# postgres trigger

### 使用


##### 1. 创建触发器函数


```
create function trigger_func_name() returns trigger as $$
begin
	code
end;
$$ language plpgsql;
```

##### 2. 创建触发器


```
create trigger trigger_name before|after trigger_action
on tbl_name for each row execute procedure trigger_func_name();
```

