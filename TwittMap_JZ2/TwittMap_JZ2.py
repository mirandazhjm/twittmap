from flask import Flask,render_template
import json
from elasticsearch import Elasticsearch, RequestsHttpConnection
from requests_aws4auth import AWS4Auth
from key import access_key,secret_key,host,zone


awsauth = AWS4Auth(access_key, secret_key, zone, 'es')
es = Elasticsearch(
    hosts=[{'host': host, 'port': 443}],
    http_auth=awsauth,
    use_ssl=True,
    verify_certs=True,
    connection_class=RequestsHttpConnection
)
print(es.info())

'''
es.index(index='twittmap', doc_type='tweets', id=2, body={
    'text': 'hello',
    "sentiment":'neg'
})

res=es.search(index='twittmap', doc_type="tweets",body={"query":{"term":{ "text": "hello" }}})
print res['hits']['hits']
for doc in res['hits']['hits']:
    print doc['_source']['text']

'''
app = Flask(__name__)

@app.route('/')
def welcome():
    return render_template('welcome.html')

@app.route('/gmap',methods=['GET'])
def gmap():
    return render_template('gmap.html',result=json.dumps({"a":[{"o":1},{"o":2}]}, indent = 2))

#mapping
map={
    1:'Love',
    2:'Food',
    3:'Trump',
    4:'Travel',
    5:'New York',
    6:'Job',
    7:'Hillary',
    8:'Fashion',
    9:'LOL',
    10:'Vegas'
}
@app.route('/q/<int:value>',methods=['GET'])
def q(value):
    res = es.search(index='index', doc_type='twitter', size=1000, from_=0,body={"query":{'match':{"tweet": map[value]}}})
    list=[]
    for doc in res['hits']['hits']:
        tmp={}
        tmp['text']= doc['_source']['tweet']
        tmp['geo']= doc['_source']['coordinates']
        list.append(tmp)
    '''
    res=es.search(index='twittmap', doc_type="tweets",body={"query":{"term":{ "text":map[value]}}})
    #print res['hits']['hits']
    list=[]
    for doc in res['hits']['hits']:
        tmp={}
        tmp['text']=doc['_source']['text']
        tmp['user']=doc['_source']['user']
        tmp['geo']=doc['_source']['geo']
        list.append(tmp)
        #{"res":list}
    '''
    return json.dumps({'a':list})

@app.route('/w/<value>',methods=['POST'])
def w(value):
    lat=value.split('_')[0]
    lon=value.split('_')[1]
    float(lat.replace(u'\N{MINUS SIGN}', '-'))
    float(lon.replace(u'\N{MINUS SIGN}', '-'))
    res = es.search(index='index', doc_type='twitter', body={"filter" : {
                "geo_distance" : {
                    "distance" : "40km",
                    "geo" : {
                        "lat" : lat,
                        "lon" : lon
                    }
                }
            }})
    list=[]
    for doc in res['hits']['hits']:
        tmp={}
        tmp['text']=doc['_source']['text']
        tmp['user']=doc['_source']['user']
        tmp['geo']=doc['_source']['geo']
        list.append(tmp)
    print list
    '''
    res=es.search(index='twittmap', doc_type="tweets",body={"query":{'match':{"tweet": map[value]}, "filter" : {
                "geo_distance" : {
                    "distance" : "100km",
                    "geo" : {
                        "lat" : int(lat),
                        "lon" : int(lon)
                    }
                }
            }}})
    #print res['hits']['hits']
    list=[]
    for doc in res['hits']['hits']:
        tmp={}
        tmp['text']=doc['_source']['text']
        tmp['user']=doc['_source']['user']
        tmp['geo']=doc['_source']['geo']
        list.append(tmp)
        #{"res":list}
'''
    return json.dumps({"a":'T'}, indent = 2)

@app.route('/search', methods=['GET'])
def search():
    return render_template('search.html')

if __name__ == '__main__':
    app.run()
