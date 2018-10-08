# NewsClient<br>
> ## 一、简介
> 这是一个新闻客户端，实时浏览新闻。
> ## 二、详情
> 新闻数据来源自[聚合数据](https://www.juhe.cn/)的新闻API，使用原生的*JSONObject*和*JSONArray*来解析。
> * 1、结合使用**TabLayout**、**ViewPager**和**Fragment**，实现了具有当下主流新闻APP的滑动切换新闻类型功能的新闻APP。
> * 2、使用了LruCache来缓存新闻的缩略图，使用异步机制AsyncTask来下载图片。
> * 3、优化了listview，提高了加载速度，解决了错项等问题。
> * 4、自定义了数据刷新的View，实现了下拉刷新新闻数据的功能。
