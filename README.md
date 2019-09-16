# page-list


## 使用说明

加入pom

```
        <!- https://github.com/1991wangliang/page-list ->
        <dependency>
            <groupId>com.codingapi</groupId>
            <artifactId>page-list</artifactId>
            <version>0.0.1</version>
        </dependency>

```

```

	@Autowired
	private PageHelper pageHelper;

	@Test
	public void test()throws Exception {
		//select * from t_book where name like '%1%' order by age

		//t_books

		List<Book> books1 = bookDao.sql("select * from t_book_1 where name like '%1%' order by age desc");
		pageHelper.initData("t_books",Book.class,books1,"list");

		List<Book> books2 = bookDao.sql("select * from t_book_2 where name like '%1%' order by age desc");
		pageHelper.initData("t_books",Book.class,books2,"list");

		List<Book> books3 = bookDao.sql("select * from t_book_3 where name like '%1%' order by age desc");
		pageHelper.initData("t_books",Book.class,books3,"list");


		//汇总之后再查询数据
		List<Book> list = pageHelper.query("select * from t_books order by age desc limit 1,2", new ResultSetHandler<List<Book>>() {
			@Override
			public List<Book> handle(ResultSet resultSet) throws SQLException {
				List<Book> list = new ArrayList<>();
				while (resultSet.next()){
					Book book = new Book();
					book.setAge(resultSet.getInt("age"));
					book.setId(resultSet.getLong("id"));
					book.setName(resultSet.getString("name"));
					book.setTime(resultSet.getTime("time"));
					book.setList(JSONObject.parseArray(resultSet.getString("list"),Item.class));
					list.add(book);
				}
				return list;
			}
		});

		log.info("list->{}",list);

        //测试删除，实际什么时候删除后面会增加策略删除数据库的
		pageHelper.dropTable("t_books");


	}

```