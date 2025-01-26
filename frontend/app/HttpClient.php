<?php

namespace Ehva\AgencyteamFront;

class HttpClient
{
    private string $baseUrl;
    private array $headers;
    private int $timeout;

    /**
     * @param string      $baseUrl
     * @param array       $headers
     * @param int|integer $timeout
     */
    public function __construct(string $baseUrl, array $headers = [], int $timeout = 30)
    {
        $this->baseUrl = rtrim($baseUrl, '/');

        $this->headers = array_merge([
            'Content-Type: application/json',
            'Accept: application/json'
        ], $headers);

        $this->timeout = $timeout;
    }

    /**
     * @param  string $endpoint
     * @param  array  $params  
     * 
     * @return array
     */
    public function post(string $endpoint, array $params = []): array
    {
        return $this->request('POST', $endpoint, $params);
    }

    /**
     * @param  string $endpoint
     * @param  array  $params  
     * 
     * @return array
     */
    public function delete(string $endpoint, array $params = []): array
    {
        return $this->request('DELETE', $endpoint, $params);
    }

    /**
     * @param  string $endpoint
     * @param  array  $params  
     * 
     * @return array
     */
    public function get(string $endpoint, array $params = []): array
    {
        $query = !empty($params) ? '?' . http_build_query($params) : '';
        return $this->request('GET', $endpoint . $query);
    }

    /**
     * @param  string $endpoint
     * @param  array  $params  
     * 
     * @return array
     */
    public function put(string $endpoint, array $params): array
    {
        return $this->request('PUT', $endpoint, $params);
    }

    /**
     * @param  string $method  
     * @param  string $endpoint
     * @param  array  $params  
     * 
     * @return array
     */
    private function request(string $method, string $endpoint, array $params = []): array
    {
        $curl = curl_init();
        $url = $this->baseUrl . '/' . ltrim($endpoint, '/');

        $options = [
            CURLOPT_URL => $url,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_ENCODING => '',
            CURLOPT_MAXREDIRS => 5,
            CURLOPT_TIMEOUT => $this->timeout,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => $method,
            CURLOPT_HTTPHEADER => $this->headers,
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_SSL_VERIFYHOST => 0,
        ];

        if (!empty($params) && $method !== 'GET') {
            $options[CURLOPT_POSTFIELDS] = json_encode($params);
        }

        curl_setopt_array($curl, $options);

        $response = curl_exec($curl);
        $statusCode = curl_getinfo($curl, CURLINFO_HTTP_CODE);
        $error = curl_error($curl);

        curl_close($curl);

        if ($error) {
            return [
                'status_code' => 500,
                'response' => $error
            ];
        }

        $decodedResponse = json_decode($response, true);
        
        if (json_last_error() !== JSON_ERROR_NONE) {
            throw new \Exception('Failed to decode JSON response: ' . json_last_error_msg());
        }

        if ($statusCode >= 400) {
        	return [
        		'status_code' => $statusCode,
        		'response' => $decodedResponse
        	];
        }

        return $decodedResponse;
    }
}