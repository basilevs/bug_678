# Reproduce the issue [678][1]

## Prerequisites
Docker

## Test steps
Run `docker compose up` in the directory with compose.yaml file

## Expected result

`test-1 exited with code 0`

## Actual result

```
test-1  | qemu: uncaught target signal 6 (Aborted) - core dumped
test-1  | Aborted
test-1 exited with code 134
```


[1]: https://github.com/eclipse-platform/eclipse.platform.swt/issues/678