# Brief API Documentation for Location Streamer
## Main API URL **https://plaxo-location-app.herokuapp.com/v1/staff-service/**

## Error Statuses
*true* **An error occurred**
*false* **No error occurred**

## HTTP Error Codes
**This feature is still in progress so as to maintain standard practice. However error codes 200, 404 and 500 are currently available and the maintain standard HTTP Response meanings**

## Endpoints
*/all-users* **GET Request. This endpoint returns all users registered on the location platform. Future work will be done to limit list to organization**

*/login* **POST Request. This endpoint accepts username and password and returns payload indicating if user is authorized or not**

- Parameters: 
	- username: Username created during staff registration
 	- password: password created during staff registration

*/new-staff* **POST Request. This endpoint creates a new user whose device will stream location data to service**

- Parameters:
	- username: username for staff or biker
	- password: secret password for staff or biker
	- email: email address of staff or biker
	- staff_id: unique staff id assigned
	- mobile: mobile number of staff or biker
	- device_id: unique device identifier for biker's mobile device

*/location-update* **POST Request. This endpoint accepts user/biker's location updates at intervals. This endpoint may be deprecated in future to ensure that location updates are streamed via a socket connection which is more reliable.**

- Parameters:
	- device_id: device identifier registered when staff/biker was created
	- device_type: tablet, mobile, IoT device
	- payload: this is a JSON string containing necessary location parameters to be sent to the API
