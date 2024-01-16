import http from 'k6/http';
import {check, group} from 'k6';

const isNumeric = (value) => /^\d+$/.test(value);

const default_vus = 5;

const target_vus_env = `${__ENV.TARGET_VUS}`;
const target_vus = isNumeric(target_vus_env) ? Number(target_vus_env) : default_vus;

export let options = {
    stages: [
        { duration: '10s', target: target_vus },
        { duration: '1m', target: target_vus },
        { duration: '10s', target: target_vus },
    ],
};

const BASE_URL = 'http://product-service:8080/api/v1/products';

export default () => {

    group('Find Product by ID', function () {
        let productId = 1;
        let res = http.get(`${BASE_URL}/${productId}`);
        check(res, { 'status is 200': (r) => r.status === 200 });
    });

    group('Find All Products', function () {
        let queryParams = {
            name: "TestProduct",
            active: "true",
            sortBy: "NAME",
            directionSort: "ASC",
            pageNumber: "0",
            pageSize: "10"
        };
        let url = `${BASE_URL}?` + Object.keys(queryParams)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(queryParams[key])}`)
            .join('&');
        let res = http.get(url);
        check(res, { 'status is 200': (r) => r.status === 200 });
    });

    group('Create Product', function () {
        let productCreatePayload = JSON.stringify({
            name: "New Product",
            active: true,
            price: 100.00,
            currency: "USD"
        });
        let headers = {
            'Content-Type': 'application/json',
        };
        let res = http.post(`${BASE_URL}`, productCreatePayload, { headers: headers });
        check(res, { 'status is 200': (r) => r.status === 200 });
    });
}
