Here a script to clone, install, deploy and run all reference architecture projects.

## How use

Download the [ref-arch.zip](./.attachments/ref-arch-90833873-126e-4c30-88de-eae47ef83277.zip) and unzip it in the folder you want.

## Init

Open a terminal, navigate the previous folder and execute the following command:

`ref-arch.bat clone install image run`

## Manually check

To check everything was well, when your docker-compose is up, you can run the following http requests:

- Retrieve a valid access token
`curl -X POST "http://localhost:8760/companymanagement/auth/token" -H "accept: */*" -H "Content-Type: application/json" -d "{\"companyName\":\"CHOAM\"}"
`
- Get the previous access token, replace <_accessToken_> and validate a mortgage with this command :
`curl -X POST "http://localhost:8760/mortgagefeasibility/validate" -H "accept: */*" -H "Authorization: <accessToken>" -H "Content-Type: application/json" -d "{\"city\":\"string\",\"company\":\"string\",\"country\":\"string\",\"downPayment\":810,\"homePrice\":9999.9,\"loanLength\":0,\"monthlyIncomes\":100.1}"`
- Finally, you can confirm a mortgage (replace <_mortgageId_> with a correct mortgage id and <_accessToken_> with a valid access token):
`curl -X POST "http://localhost:8760/mortgagefeasibility/confirm?mortgageId=<mortgageId>" -H "accept: */*" -H "Authorization: <accessToken>" -d ""`