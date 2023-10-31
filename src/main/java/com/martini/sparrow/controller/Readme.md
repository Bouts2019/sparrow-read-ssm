- 本层下的Controller不进行切面操作
- restful层下的RestController需要进行切面操作

- 本层下的Controller对于异常主要为404异常，提供统一的404异常处理
- restful层下的异常处理由于进行了切面操作，因此都是一致的返回错误信息。