# regurgitator-extensions-web-yml

regurgitator is a lightweight, modular, extendable java framework that you configure to 'regurgitate' canned or clever responses to incoming requests; useful for quickly mocking or prototyping services without writing any code. simply configure, deploy and run.

start your reading here: [regurgitator-all](https://talmeym.github.io/regurgitator-all#regurgitator)

## extension web steps in yml

### http-call 

an http-call step make an outward http call

```yml
http-call:
 host: http://otherservice.com
 port: 80
 username: username
 password: password
 steps: [
 - create-response:
    source: response-metadata:status-code
    processor:
     freemarker-processor:
      value: http call returned ${value}
```

while host, port and (optional) user credentials are specified in the yml, all other attributes of the call are set from parameters within the message object, as listed below:

|context|parameter|call attribute|default (if not specified)|
|:---|:---|:---|:---|
|``request-metadata``|``method``|method|GET|
|``request-metadata``|``path-info``|path|/|
|``request-headers``|``[header name]`` | http header |not set|
|``request-payload``|``text`` (for POST calls) |payload|not set|
|``request-metadata``|``content-type`` (for POST calls) |content type|not set|
|``request-metadata``|``character-encoding`` (for POST calls) |character encoding|not set|

#### http proxy

if using the ``RegurgitatorServlet`` to mock http requests, an ``http-call`` step not placed within an isolated sequence will, upon execution, be given a message object that already contains the ``request-metadata`` context from the incoming http call, and will therefore act as an http proxy, making an http call identical to the one received by the ``RegurgitatorServlet``, except redirected to a new endpoint. this is useful for forwarding on any calls you wish not to mock / implement.

If instead you wish the http-call step to make an independent call, then the step should be placed within an isolated sequence, and should be proceeded by ``create-parameter`` steps to set the necessary method, path and header metadata.

```yml
sequence:
 isolate: true
 steps:
 - create-parameter:
    name: request-metadata:method
    value: GET
 - create-parameter:
    name: request-metadata:path-info
    value: /service-api
 - http-call:
    host: http://otherservice.com
    port: 80
    username: username
    password: password
    steps:
    - create-response:
       source: response-metadata:status-code
       processor:
        freemarker-processor:
         value: http call returned ${value}
}
```

the ``steps`` property of the http-call step can contain zero or more "response processing" steps, to be run when a response is received from the outgoing http call. these steps are given a new message object containing all the metadata associated with the http response (contexts such as ``response-metadata``, ``response-payload`` etc.) as well as the ``parameters`` and ``session`` contexts of the original message. 

### create-http-response

a create-http-response step returns a response from regurgitator, allowing ``response-metadata`` parameters to be set for ``content-type`` and ``status-code``.

```yml
create-http-response:
 source: response
 content-type: application/json
 status-code: 200
}
```

this step is primarily a convenience step, aggregating the following core config:

```yml
- create-parameter:
   name: response-metadata:content-type
   value: application/json
- create-parameter:
   name: response-metadata:status-code
   value: 200
- create-response:
   source: response
```

### create-file-response

a create-file-response step returns a response from regurgitator, with its contents read from a file on the classpath, with the name of the file sourced from a parameter value. this is useful when you receive an http request for a file, and you wish to simply return the contents of that file, if it exists.

```yml
create-file-response:
 source: request-metadata:path-info
 path-prefix: /assets
```

the optional ``path-prefix`` attribute specifies a path fragment to be prefixed to the value found in the parameter, making a request for ``/file.xml`` actually load and return ``classpath:/assets/file.xml``

if the requested file does not exist on the classpath ``response-metadata:status-code`` is set to ``404`` and the step responds with ``Not Found``.

## extension web constructs in yml

### query-param-processor

a query-param-processor processes a parameter value, extracting a value from within an http query string.

```yml
create-parameter:
 value: something=this&id=that&third=another
 processor:
  query-param-processor
   key: id
```

if the key appears in the query string more than once, the processor will aggregate the values into a collection.
