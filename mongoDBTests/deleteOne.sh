curl --location --request POST 'https://data.mongodb-api.com/app/data-rlgbq/endpoint/data/beta/action/deleteOne' \
--header 'Content-Type: application/json' \
--header 'Access-Control-Request-Headers: *' \
--header 'api-key: TUGyzJPmesVH4FcrDqO0XovgYNq0L5B59xCnjFsB9nLFE7qkofdTvzYjBn2ID120' \
--data-raw '{
    "collection":"Jars",
    "database":"TikoJarTest",
    "dataSource":"PositivityJar",
    "filter": { "serverID": "ABC500" }
}' | json_pp