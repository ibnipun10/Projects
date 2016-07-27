from __future__ import print_function

import json
import urllib
import boto3
import requests

print('Loading function')

s3 = boto3.client('s3')
PUBLISH = 'publish'
UNPUBLISH = 'unpublish'
DELETE_USER = 'deleteUser'
INSERT_USER = 'insertUser'
ES_URL = 'https://search-swyftapi-es-test-vm3u2vzwg6kjtuep2px2p7w3hq.us-east-1.es.amazonaws.com/'
INDEX = 'swyft'
INDEX_TYPE_ASSET = 'assets'
INDEX_TYPE_USER = 'users'
HEADERS = {'content-type': 'application/json'}
INDEXID = '{ "index": { "_id" : "%s" }}'
DELETEID = '{ "delete": { "_id" : "%s" }}'
BULK = '_bulk'

FLAG_INSERT = 1
FLAG_DELETE = 2

HTTP_POST = 'POST'
HTTP_DELETE = 'DELETE'

def getAssetUrl():    
    return ES_URL + INDEX + '/' + INDEX_TYPE_ASSET 

def getUserUrl():
    return ES_URL + INDEX + '/' + INDEX_TYPE_USER

def sendToES(url, data, op=HTTP_POST):
    
    if(op == HTTP_POST):
        r = requests.post(url, data, headers=HEADERS)
        print('response', r.json())
    elif(op == HTTP_DELETE):
        r = requests.delete(url)
        print('response', r.json())
        
    
def assets(data):    

    # get bulk api
    url = getAssetUrl() + '/' + BULK
    print('publish asset url ', url)
    js = json.loads(data)
    payload = ''
    for x in js:
        print('asset data ', x)
        index = INDEXID % x['id']
        payload = payload + index + '\n'
        payload = payload + json.dumps(x) + '\n'

    print('payload to send ', payload)
    sendToES(url, payload)

def user(data, op):
    
    url = getUserUrl()
    print('publish asset url ', url)
    print('data loaded from file', data)
    js = json.loads(data)
    newurl = url + '/' + js['userId']
    
    if op == FLAG_INSERT:
        
        payload = data
        sendToES(newurl, payload)
        print('payload to send ', payload)
    
    elif op == FLAG_DELETE:
        print('delete operation')
        sendToES(newurl, None, HTTP_DELETE)
        
        

def lambda_handler(event, context):
    #print("Received event: " + json.dumps(event, indent=2))

    # Get the object from the event and show its content type
    bucket = event['Records'][0]['s3']['bucket']['name']
    key = urllib.unquote_plus(event['Records'][0]['s3']['object']['key']).decode('utf8')
    try:
        response = s3.get_object(Bucket=bucket, Key=key)
        data = response["Body"].read()
        
        if UNPUBLISH in key:
            print('unpublish')
            assets(data)
        elif INSERT_USER in key:
            print('insertUser')
            user(data, FLAG_INSERT)
        elif DELETE_USER in key:
            print('deleteUser')
            user(data, FLAG_DELETE)
        elif PUBLISH in key:
            print('publish')
            assets(data)
        
        return response['ContentType']
    
    except Exception as e:
        print(e)
        print('Error getting object {} from bucket {}. Make sure they exist and your bucket is in the same region as this function.'.format(key, bucket))
        raise e