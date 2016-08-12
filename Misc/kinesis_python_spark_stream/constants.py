from pyspark.streaming.kinesis import InitialPositionInStream
from pyspark.storagelevel import StorageLevel

NUM_STREAMS = 4
NUM_EXECUTORS = 4
NUM_CORES = 4
NUM_PARTITIONS = 1 * NUM_CORES * NUM_EXECUTORS
NUM_TRUE_CORES = 7     #change this value according to executors cores

HOME_PATH = '/home/hadoop'
FILE_PATH = '/POC/kinesis_python_spark_stream'
CODE_PATH = HOME_PATH + FILE_PATH

COLUMNS = ['uri', 'projectid', 'ip', 'timestamp', 'useragent', 'referrer', 'httpstatus']
COLUMN_TYPES = ['string', 'string', 'string', 'string', 'string', 'string', 'string']

COL_STARTTIME = 'startTime'
COL_ENDTIME = 'endTime'
COL_CUSTOMERID = 'CustomerId'
COL_PROJECTID = 'projectId'
COL_FONTTYPE = 'fontType'
COL_FONTID = 'fontId'
COL_DOMAINNAME = 'domainName'
COL_USERAGENT = 'userAgent'
COL_IPADDRESS = 'ipAddress'
COL_GEOLOCATION = 'geoLocation'
COL_PAGEVIEWCOUNT = 'webfontusage'

PROJECTID = 'projectId'

#redshift cred
REDSHIFT_HOSTNAME = ''
REDSHIFT_PORT = '5439'
REDSHIFT_USERNAME = ''
REDSHIFT_PASSWORD = ''
REDSHIFT_DATABASE = ''
REDSHIFT_PAGEVIEW_TBL = ''
REDSHIFT_PAGEVIEWGEO_TBL = ''
REDSHIFT_URL = ("jdbc:redshift://" + REDSHIFT_HOSTNAME + ":" + REDSHIFT_PORT + "/" +   REDSHIFT_DATABASE + "?user=" + REDSHIFT_USERNAME + "&password="+ REDSHIFT_PASSWORD)

#kinesis cred
APPLICATION_NAME = 'samsanalyticsprocessor'
STREAM_NAME = 'SAMSAnalytics'
REGION_NAME = 'us-east-1'
INITIAL_POS = InitialPositionInStream.LATEST
CHECKPOINT_INTERVAL = 60
ENDPOINT = ''
AWSACCESSID = ''
AWSSECRETKEY = ''
STORAGE_LEVEL = StorageLevel.MEMORY_ONLY

#S3 cred
S3ACCESSID = ''
S3SECRETKEY = ''
BUCKET = 'sams-analytics-poc'
FOLDER = 'sams-poc'
S3_URL = 's3n://' + BUCKET + '/' + FOLDER

FONT_LIST = ['ttf', 'woff', 'woff2', 'svg', 'eot', 'ttf-1', 'woff-3', 'woff2-14', 'svg-11', 'eot-2']

#table type
PAGEVIEW_TYPE = 1
PAGEVIEWGEO_TYPE = 2
