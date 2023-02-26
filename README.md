# Fa4nir lib

## Fa4nir description

Project oriented on minimize efforts when we work with legacy listeners (not only). The main idea is define interface
which annotated as Transmitter and extends legacy interface and you can define fallback and method which you want to
notify in Receiver class. Receiver class is your default POJO class with contains method which we want to notify and
fallback method  
and methods for delegate result. And after this declaration on compile steps fa4nir will generate code which you can use
to listening your notifications.

## Build

```
    gradle clean jar fatJar
```

## Tests

```
    gradle :fa4nir-test:test
```

## Dependencies 
