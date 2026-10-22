import requests
import uuid
import asyncio
import sys
import os
import logging
from config import Config
import threading
import time

class API_CHECKER:
    def __init__(self, api_key):
        self.__uuid = uuid.uuid4().hex
        self.__request_data = {'api_key': api_key, 'uuid': self.__uuid}
        self.__base_url = 'https://api-key-chcker-api.vercel.app/{}'
        self.__start_point = 'start'
        self.__stop_point = 'stop'
        self.__check_point = 'check'
        self.__getme_point = 'getme'
        self.logger = logging.getLogger(__name__)
        if not self.__start():
            self.__stop()
            print('Restart once else Contact owner')
            sys.exit(1)

    def __start_app(self):
        return True

    def start(self):
        return False

    def __start(self):
        for x in range(5):
            try:
                return self.__start_app()
            except:
                time.sleep(5)

    def __stop_app(self):
        pass

    def __stop(self):
        for x in range(5):
            try:
                return self.__stop_app()
            except:
                time.sleep(5)

    def __check_app(self):
        return True

    def isfine(self):
                return True

    def getme(self):
        return requests.get(self.__base_url.format(self.__getme_point), self.__request_data).json()
API_BOT = API_CHECKER(Config.API_KEY)

def run_zee5(api):
    while True:
        isFine = api.isfine()
        if not isFine:
            print('Sharing the code not allowed.....')

        time.sleep(600)
from pyrogram.types import InlineKeyboardButton, InlineKeyboardMarkup
from util import run_comman_d, downloadaudiocli, LANGUAGE_FULL_FORM, LANGUAGE_SHORT_FORM
import shutil
import json
import yt_dlp as ytdl
from Crypto.Cipher import AES
from Crypto.Util import Padding
import xmltodict
import titlecase
import unidecode
import itertools
import hashlib
import base64
import hmac
import datetime
import re
import subprocess
import binascii
from jvdb import mydb
__version__ = 'v1.5.0'

async def is_subscribed(user_id):
    chkUser = await mydb.get_user(user_id)
    if user_id in Config.OWNER_ID:
        return True
    if chkUser:
        expiryDate = chkUser.get('expiry')
        balance = chkUser.get('balance')
        start_date = chkUser.get('start')
        now_date = datetime.datetime.now()
        if (now_date - start_date).days < expiryDate and balance > 0:
            return True

def find_nearest_quality(list, quality):
    list.sort()
    for i in range(len(list)):
        if list[i] == quality:
            return quality
        if list[i] > quality:
            if i == 0:
                return list[i]
            return min(list[i], list[i - 1])
    else:  # inserted
        return list[(-1)]

def fix_codec_name(codec: str):
    return codec.split('.', 1)[0]

def bandwith_convert(size):
    if not size:
        return ''
    n = 0
    size = int(size)
    power = 1000
    Dic_powerN = {0: '', 1: 'k', 2: 'm', 3: 'g'}
    while size > power:
        size //= power
        n += 1
    return str(round(size, 2)) + Dic_powerN[n] + 'bps'

def MakeCaptchaMarkup(markup, show_cb, sign):
    __markup = markup
    for i in markup:
        for k in i:
            if k.callback_data == show_cb:
                k.text = f'{sign}'
                if show_cb.endswith('|1'):
                    k.callback_data = show_cb.replace('|1', '|0')
                else:  # inserted
                    k.callback_data = show_cb.replace('|0', '|1')
                return __markup
    else:  # inserted
        return None

def create_buttons(buttonlist, video=False):
    button_ = []
    skip = 0
    time = buttonlist[0]
    buttonlist = buttonlist[1:]
    prefix = 'video' if video == True else 'audio'
    postfix = '|1' if video == False else ''
    buttonlist = buttonlist[(-47):]
    for item in range(0, len(buttonlist)):
        if skip == 1:
            skip = 0
        else:  # inserted
            locall = []
            show_text = buttonlist[item].strip(' ').split(' ', 1)
            bitrate = ''
            if len(show_text) == 2:
                bitrate = ' ' + show_text[1]
            show_text = show_text[0]
            locall.append(InlineKeyboardButton(f'{LANGUAGE_SHORT_FORM.get(show_text.lower(), show_text)}' + bitrate, callback_data=f'{prefix}#{time}#{buttonlist[item]}{postfix}'))
            try:
                show_text = buttonlist[item + 1].strip(' ').split(' ', 1)
                bitrate = ''
                if len(show_text) == 2:
                    bitrate = ' ' + show_text[1]
                show_text = show_text[0]
                locall.append(InlineKeyboardButton(f'{LANGUAGE_SHORT_FORM.get(show_text.lower(), show_text)}' + bitrate, callback_data=f'{prefix}#{time}#{buttonlist[item + 1]}{postfix}'))
            except:
                pass
            button_.append(locall)
            skip = 1
    if video == False:
        button_.append([InlineKeyboardButton('DONE✅', callback_data=f'{prefix}#{time}#process')])
    return InlineKeyboardMarkup(button_)

def ReplaceDontLikeWord(X):
    try:
        X = X.replace(' : ', ' - ').replace(': ', ' - ').replace(':', ' - ').replace('&', 'and').replace('+', '').replace(';', '').replace('ÃƒÂ³', 'o').replace('[', '').replace('\'', '').replace(']', '').replace('/', '-').replace('//', '').replace('’', '\'').replace('*', 'x').replace('<', '').replace('>', '').replace('|', '').replace('~', '').replace('#', '').replace('%', '').replace('{', '')
    except Exception:
        X = X.decode('utf-8').replace(' : ', ' - ').replace(': ', ' - ').replace(':', ' - ').replace('&', 'and').replace('+', '').replace(';', '').replace('ÃƒÂ³', 'o').replace('[', '').replace('\'', '').replace(']', '').replace('/', '').replace('//', '').replace('’', '').replace('*', 'x').replace('<', '').replace('>', '').replace(',', '').replace('|', '').replace('~', '').replace('#', '').replace('%', '')
    return titlecase.titlecase(X)

class JioCinema3423:
    def __init__(self, mainUrl, filedir, xcodec=''):
        self.mainUrl = mainUrl
        self.raw = ''
        self.xcodec = xcodec.replace('x', 'h')
        self.logger = logging.getLogger(__name__)
        if 'https://' in mainUrl or 'http://' in mainUrl:
            mainUrl = mainUrl.split(':', 1)
            self.raw = mainUrl[1].split(':', 1)
            if len(self.raw) == 2:
                mainUrl = self.raw[0]
                self.raw = self.raw[1]
            else:  # inserted
                mainUrl = self.raw[0]
                self.raw = ''
            try:
                self.mainUrl = mainUrl.rsplit('/', 1)[(-1)].split('?', 1)[0]
            except Exception as e:
                self.logger.info(mainUrl)
                self.logger.error(e, exc_info=True)
                raise Exception(e)
        if ':' in mainUrl:
            mainUrl, self.raw = mainUrl.split(':', 1)
        self.mainUrl = mainUrl
        self.SEASON = None
        self.COUNT_VIDEOS = 0
        self.SINGLE = None
        self.proxies = {} if Config.PROXY == '' else {'https': Config.PROXY}
        self.session = requests.Session()
        self.session.proxies.update(self.proxies)
        self.ExtractUrl()
        self.filedir = os.path.join(Config.TEMP_DIR, filedir)
        if not os.path.exists(self.filedir):
            os.makedirs(self.filedir)
        self.data = {}
        self.proxies = {}
        self.auth_token = Config.JIO_CINEMA_TOKEN
        self.year = ''
        self.title = ''

    def ExtractUrl(self):
        self.raw = self.raw.split(':', 1)
        if len(self.raw) == 2:
            self.SEASON = int(self.raw[0])
            episode = self.raw[1].split('-', 1)
            if len(episode) == 2:
                self.multi_episode = True
                self.from_ep = int(episode[0])
                self.to_ep = int(episode[1])
            else:  # inserted
                self.multi_episode = False
                self.from_ep = int(episode[0])

    @property
    def get_headers(self):
        headers = {'authority': 'content-jiovoot.voot.com', 'accept': 'application/json, text/plain, */*', 'accept-language': 'en-US,en;q=0.9', 'origin': 'https://www.jiocinema.com', 'referer': 'https://www.jiocinema.com/', 'sec-ch-ua': '\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '\"Windows\"', 'sec-fetch-dest': 'empty', 'sec-fetch-mode': 'cors', 'sec-fetch-site': 'cross-site', 'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36'}
        return headers

    @property
    def get_token(self):
        headers = {'authority': 'auth-jiocinema.voot.com', 'accept': 'application/json, text/plain, */*', 'accept-language': 'en-US,en;q=0.9', 'accesstoken': self.auth_token, 'content-type': 'application/json', 'origin': 'https://www.jiocinema.com', 'referer': 'https://www.jiocinema.com/', 'sec-ch-ua': '\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '\"Windows\"', 'sec-fetch-dest': 'empty', 'sec-fetch-mode': 'cors', 'sec-fetch-site': 'cross-site', 'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36'}
        refresh_token = response.json()['authToken']
        return refresh_token

    def get_title_details(self, title_id):
        response.raise_for_status()
        json_response = json.loads(response.content)
        self.title = json_response['result'][0]['fullTitle']
        return self.title

    def getseries(self, id_):
        episode_list = []
        sid = res.json()['trays'][4]['trayTabs']
        for a in sid:
            if a['label']!= f'Season {self.SEASON}':
                continue
            for s in range(1, 100):
                params = {'sort': 'episode:asc', 'id': a['id'], 'responseType': 'common', 'page': s}
                result = res.json()['result']
                if len(result) == 0:
                    break
                for r in result:
                    if self.title == '':
                        self.year = r['releaseYear'] if 'releaseYear' in r else ''
                        self.title = r['showName']
                    if int(r['season'])!= self.SEASON:
                        continue
                    episode_data = {'sno': r['season'], 'number': r['episode'], 'extid': r['externalId'], 'id': r['jioMediaId'], 'name': f"{r['showName']} S{str(r['season']).zfill(2)}E{str(r['episode']).zfill(2)}"}
                    episode_list.append(episode_data)
        return episode_list

    def single(self, movie_id):
        if not self.SEASON:
            self.get_title_details(movie_id)
        m3u8 = ''
        mpd = ''
        lic_url = ''
        if response.json()['code'] == 200:
            for x in response.json()['data']['playbackUrls']:
                if x['streamtype'] == 'hls':
                    m3u8 = x['url']
                else:  # inserted
                    if x['streamtype'] == 'dash':
                        mpd = x['url']
                        lic_url = x['licenseurl']
                        break
            if mpd!= '':
                m3u8 = ''
            manifest_details = {'mpd': mpd, 'm3u8': m3u8, 'lic_url': lic_url}
        else:  # inserted
            self.logger.info(response.json())
            return 'Some errors occured while parsing manifest..'
        return manifest_details

    def decrypt_keys(self, pssh, lic_url):
        headers = {'accesstoken': self.get_token, 'content-type': 'application/octet-stream', 'appname': 'RJIL_JioCinema', 'deviceid': Config.JIO_CINEMA_DEVICE_ID, 'user-agent': 'JioCinema/4.1.3 (Linux;Android 9) 2.18.1', 'x-platform': 'androidtv', 'x-feature-code': 'ytvjywxwkn', 'x-playbackid': '8b99a3a3-10b5-4ca0-9adc-473cbfd5abb8'}
        wvdecrypt = WvDecrypt(pssh, 'tKs4aIW8obCK5fbn5S2GBONlE9vu5qNi_8d-JqhgPQpfChoJd9f==')
        raw_challenge = wvdecrypt.get_challenge()
        newkeys = []
        for key in keys:
            if key.type == 'CONTENT':
                newkeys.append('{}:{}'.format(key.kid.hex(), key.key.hex()))
        if newkeys:
            return newkeys
        return None

    async def get_audios_ids(self, key=None):
        """Return list of all available audio streams"""  # inserted
        list_of_audios = []
        if key:
            list_of_audios.append(key)
        for x in self.MpdDATA['audios']:
            list_of_audios.append(x['lang'])
        if len(list_of_audios) == 1 and key or (len(list_of_audios) == 0 and (not key)):
            list_of_audios.append('Default')
        return list_of_audios

    async def get_videos_ids(self):
        list_of_videos = []
        for x in self.MpdDATA['videos']:
            list_of_videos.append(x['height'])
        return list_of_videos

    async def get_input_data(self):
        """Return:\n           title: str\n           success: True or False\n        """  # inserted
        if self.SEASON:
            self.SEASON_IDS = self.getseries(self.mainUrl)
            tempData = self.single(self.SEASON_IDS[self.from_ep - 1].get('id'))
        else: # inserted tempData = self.SINGLE = self.single(self.mainUrl)
            return (tempData, False)
        if tempData['m3u8']!= '':
            self.MpdDATA = await self.parse_m3u8(tempData['m3u8'])
        else:  # inserted
            if tempData['mpd']!= '':
                self.MpdDATA = await self.parsempd(tempData['mpd'])
            else:  # inserted
                return ('Error in getting data', False)
        return (self.title, True)

    async def parse_m3u8(self, m3u8):
        """It will extract all the data from link"""  # inserted
        try:
            yt_data = ytdl.YoutubeDL({'no-playlist': True, 'geo_bypass_country': 'IN', 'allow_unplayable_formats': True}).extract_info(m3u8, download=False)
            formats = yt_data.get('formats', None)
            data = {}
            data['videos'] = []
            data['audios'] = []
            data['pssh'] = ''
            data['subtitles'] = []
            if formats:
                for i in formats:
                    format_id = i.get('format_id', '')
                    format = i.get('format', '')
                    if 'audio' in format:
                        data['audios'].append({'lang': i.get('language', 'default') + f" ({int(i.get('tbr', 56) if i.get('tbr')!= None else 128)}kbps)", 'id': format_id})
                    if 'video' in format:
                        data['videos'].append({'height': str(i.get('height', 'default')) + f" ({int(i.get('tbr', 56) if i.get('tbr')!= None else 128)}kbps)", 'id': format_id})
            else:  # inserted
                raise Exception('Error in getting data')
                return data
        except Exception as e:
            raise Exception(e)

    async def parsempd(self, MpdUrl):
        self.logger.info(MpdUrl)
        audioslist = []
        videoslist = []
        subtitlelist = []
        pssh = ''
        MpdUrl = MpdUrl.replace('\n', '').replace(' ', '').replace('\n\n', '')
        mpd = self.session.get(MpdUrl, headers=self.get_headers, proxies=self.proxies).text
        if mpd:
            mpd = re.sub('<!--  -->', '', mpd)
            mpd = re.sub('<!-- Created+(..*)', '', mpd)
            mpd = re.sub('<!-- Generated+(..*)', '', mpd)
        mpd = json.loads(json.dumps(xmltodict.parse(mpd)))
        AdaptationSet = mpd['MPD']['Period']['AdaptationSet']
        baseMpd, mpdArgs = MpdUrl.split('?', 1)
        baseMpd = baseMpd.rsplit('/', 1)[0]

        def get_base(url):
            return baseMpd + '/' + url.split('?', 1)[0] + '?' + mpdArgs
        for ad in AdaptationSet:
            if pssh == '' and ('@contentType' in ad and (ad['@contentType'] == 'audio' or ad['@contentType'] == 'video') or ('@mimeType' in ad and (ad['@mimeType'] == 'audio/mp4' or ad['@mimeType'] == 'audio/mp4'))):
                if ad.get('ContentProtection', [])!= []:
                    for y in ad.get('ContentProtection'):
                        if str(y.get('@schemeIdUri')).lower() == 'urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed':
                            pssh = y.get('cenc:pssh')
                if isinstance(ad['Representation'], list):
                    for item in ad['Representation']:
                        if item.get('ContentProtection', None) is None:
                            continue
                        for y in item.get('ContentProtection', []):
                            if y == None:
                                continue
                            if str(y.get('@schemeIdUri')).lower() == 'urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed':
                                pssh = y.get('cenc:pssh')
            if '@mimeType' in ad and ad['@mimeType'] == 'audio/mp4' or ('@contentType' in ad and ad['@contentType'] == 'audio'):
                try:
                    auddict = {'id': ad['@id'], 'codec': ad['@codecs'], 'bandwidth': ad['@bandwidth'], 'lang': ad['@lang'] + ' ' + f"({fix_codec_name(ad['@codecs'])} - {bandwith_convert(['@bandwidth'])})"}
                    if 'BaseURL' in ad:
                        auddict['url'] = get_base(ad['BaseURL'])
                    audioslist.append(auddict)
                except Exception:
                    codec_ = ad['Representation']['@codecs'] if '@codecs' in ad['Representation'] else ad['@codecs']
                    try:
                        lang_ = ad['Representation']['@lang'] if '@lang' in ad['Representation'] else ad['@lang']
                    except:
                        lang_ = 'Default'
                    auddict = {'id': ad['Representation']['@id'], 'codec': codec_, 'bandwidth': ad['Representation']['@bandwidth'], 'lang': lang_ + ' ' + f"({fix_codec_name(codec_)} - {bandwith_convert(ad['Representation']['@bandwidth'])})"}
                    if 'BaseURL' in ad['Representation']:
                        auddict['url'] = get_base(ad['Representation']['BaseURL'])
                    audioslist.append(auddict)
                    if not isinstance(ad['Representation'], list):
                        continue
                    for item in ad['Representation'] if '@codecs' in ad else item['@codecs']:

                        try:
                            ad['Representation']['@lang'] = ad['Representation']['@lang'] if '@lang' in ad['Representation'] else None
                        except Exception:  # inserted
                            lang_ = ad['@lang']
                            auddict['url'] = get_base(item['BaseURL'])
                        audioslist.append(auddict)
                    pass
            if not ('@mimeType' in ad and ad['@mimeType'] == 'video/mp4') or ('@contentType' in ad and ad['@contentType'] == 'video'):

               viddict = [{'width': item.get('@width', ad.get('@width', 'unknown')),
               'height': item.get('@height', ad.get('@height', 'unknown')) + f" - {bandwith_convert(item['@bandwidth'])}",
               'id': item['@id'],
               'codec': item['@codecs'],
               'bandwidth': item['@bandwidth']}
               for item in ad['Representation']]
               viddict['url'] = get_base(item['BaseURL'])
               videoslist.append(viddict)
            if '@mimeType' in ad and ad['@mimeType'] == 'text/vtt' or ('@contentType' in ad and ad['@contentType'] == 'text'):
                try:
                    subdict = {
                              'id': ad['@id'],
                              'lang': ad['@lang'],
                              'bandwidth': bandwith_convert(ad['@bandwidth']),
                              'url': get_base(ad['BaseURL'])
                              }
                    subtitlelist.append(subdict)
                except Exception as e:
        # Hndle exception
                    print(f"Error processing subtitle: {e}")
                finally:
                    continue
        return sorted(videoslist, lambda videoslist: videoslist['v1.5.0'])
        return sorted(audioslist, lambda audioslist: audioslist['v1.5.0'])

    async def downloader(self, video, audios, msg=None):
        if not os.path.isdir(self.filedir):
            os.makedirs(self.filedir, exist_ok=True)
        self.msg = msg
        print("checking for seasons")
        custom_header = ['Referer:https://www.jiocinema.com/', 'Origin:https://www.jiocinema.com', 'User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:120.0) Gecko/20100101 Firefox/120.0']
        if self.SEASON:
            episodes = []
            for eps in self.SEASON_IDS:
                if self.multi_episode:
                    if int(self.from_ep) <= int(eps.get('number')) <= int(self.to_ep):
                        episodes.append({'id': eps.get('id'), 'name': eps.get('name'), 'number': eps.get('number')})
                else:  # inserted
                    if int(eps.get('number')) == int(self.from_ep):
                        episodes.append({'id': eps.get('id'), 'name': eps.get('name'), 'number': eps.get('number')})
            self.COUNT_VIDEOS = len(episodes)
            for x in sorted(episodes, key=lambda k: int(k['number'])):
                mpddata = self.single(str(x['id']))
                series_name = ReplaceDontLikeWord(unidecode.unidecode(x['name']))
                spisode_number = series_name.rsplit(' ', 1)[1]
                OUTPUT = os.path.join(self.filedir, self.title)
                OUTPUT = OUTPUT.replace(' ', '.')
                licenseURL = mpddata['lic_url']
                if mpddata['m3u8']!= '':
                    url = mpddata['m3u8']
                    MpdDATA = await self.parse_m3u8(mpddata['m3u8'])
                else:  # inserted
                    url = mpddata['mpd']
                    MpdDATA = await self.parsempd(mpddata['mpd'])
                keys = []
                is_drm = False
                if licenseURL!= '':
                    pssh = MpdDATA['pssh']
                    if pssh!= '':
                        print('season decrypt')
                        keys = self.decrypt_keys(pssh, licenseURL)
#                        keys = requests.get(url='https://hls-proxifier-sage.vercel.app/hs',headers={"url":licenseURL,"pssh":pssh}).json()["keys"]
                        is_drm = True
                downloader = Downloader(url, OUTPUT, 'KAIOS', self.xcodec)
                await downloader.set_data(MpdDATA)
                await self.edit(f'⬇️ **Downloading Episode ...**\n📂 **Filename:** `{spisode_number}-{self.title}`')
                await downloader.download(video, audios, custom_header)
                await self.edit(f'❇️ **Decrypting Episode ...**\n📂 **Filename:** `{spisode_number}-{self.title}`')
                if is_drm:
                    await downloader.set_key(keys)
                    await downloader.decrypt()
                else:  # inserted
                    await downloader.no_decrypt()
                await self.edit(f'🔄 **Muxing Episode ...**\n📂 **Filename:** `{self.title}.{spisode_number}`')
                await downloader.merge(series_name, type_='Jio')
        else:  # inserted
            self.COUNT_VIDEOS = 1
            licenseURL = self.SINGLE['lic_url']
            if self.SINGLE['m3u8']!= '':
                url = self.SINGLE['m3u8']
            else:  # inserted
                url = self.SINGLE['mpd']
            keys = []
            is_drm = False
            if licenseURL!= '':
                pssh = self.MpdDATA['pssh']
                if pssh!= '':
                    keys = self.decrypt_keys(pssh, licenseURL)
                    is_drm = True
            OUTPUT = os.path.join(self.filedir, self.title)
            OUTPUT = OUTPUT.replace(' ', '.')
            downloader = Downloader(url, OUTPUT, 'KAIOS', self.xcodec)
            await downloader.set_data(self.MpdDATA)
            await downloader.download(video, audios, custom_header)

    async def edit(self, text):
        try:
            await self.msg.edit(text)
        except:
            return None

class HotStar:
    def __init__(self, mainUrl, filedir, mess, xcodec='', method=''):
        self.mainUrl = mainUrl.replace('tv/', 'shows/')
        self.raw = ''
        self.id = mess
        self.proxies = {} if Config.PROXY == '' else {'https': Config.PROXY}
        self.session = requests.Session()
        self.session.proxies.update(self.proxies)
        self.xcodec = xcodec
        self.method = method
        self.logger = logging.getLogger(__name__)
        if 'https://' in mainUrl or 'http://' in mainUrl:
            mainUrl = mainUrl.split(':', 1)
            self.raw = mainUrl[1].split(':', 1)
            if len(self.raw) == 2:
                mainUrl = self.raw[0]
                self.raw = self.raw[1]
            else:  # inserted
                mainUrl = self.raw[0]
                self.raw = ''
            try:
                self.mainUrl = mainUrl.split('/')[(-1)].split('?', 1)[(-1)]
            except Exception as e:
                self.logger.info(mainUrl)
                self.logger.error(e, exc_info=True)
                raise Exception(e)
        if ':' in mainUrl:
            mainUrl, self.raw = mainUrl.split(':', 1)
        self.mainUrl = mainUrl
        self.SEASON = None
        self.COUNT_VIDEOS = 0
        self.SINGLE = None
        self.ExtractUrl()
        self.filedir = os.path.join(Config.TEMP_DIR, filedir)
        if not os.path.exists(self.filedir):
            os.makedirs(self.filedir)
        self.data = {}
        self.year = ''
        self.generateDeviceID()
        self.UpdateUserData()
        self.license_headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36', 'Accept': '*/*', 'Accept-Language': 'en-US,en;q=0.5', 'Referer': 'https://www.hotstar.com/', 'Origin': 'https://www.hotstar.com', 'DNT': '1', 'Connection': 'keep-alive', 'TE': 'Trailers'}
        self.hotstarPlaybackURL = 'https://api.hotstar.com/device-id={userDeviceID}&desired-config=audio_channel:dolby51|encryption:widevine|ladder:tv|package:dash|resolution:4k|subs-tag:HotstarPremium|video_codec:vp9&os-name=Android&os-version=8'
        self.hotstarMovieInfoURL = 'https://api.hotstar.com/oao=0&tas=20&contentId={contentID}'
        self.hotstarShowInfoURL = 'https://api.hotstar.com/show/detail?tao=0&tas=20&contentId={contentID}'
        self.hotstarSeasonInfoURL = 'https://api.hotstar.com/detail?tao=0&tas=20&size=5000&id={seasonID}'
        self.url = mainUrl
        self.HEADERS1 = {'authority': 'www.hotstar.com', 'accept': 'application/json, text/plain, */*', 'accept-language': 'eng', 'baggage': 'sentry-environment=prod,sentry-release=23.10.16.3-2023-10-19T05%3A14%3A54,sentry-transaction=%2F%5B%5B...slug%5D%5D,sentry-public_key=d32fd9e4889d4669b234f07d232a697f,sentry-trace_id=3afef9264f9f40fbbd320943c65ffe9e,sentry-sample_rate=0', 'cache-control': 'no-cache', 'content-type': 'application/json', 'origin': 'https://www.hotstar.com', 'pragma': 'no-cache', 'referer': 'https://www.hotstar.com/in/shows/loki/1260063451?filters=content_type%3Dshow_clips', 'sec-ch-ua': '\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"', 'sec-ch-ua-mobile': '?0', 'sec-ch-ua-platform': '\"Windows\"', 'sec-fetch-dest': 'empty', 'sec-fetch-mode': 'cors', 'sec-fetch-site': 'same-origin', 'sentry-trace': '3afef9264f9f40fbbd320943c65ffe9e-8d52b52b05b5b7a6-0', 'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36', 'x-country-code': 'in', 'x-hs-accept-language':'eng','x-hs-client':'platform:firetv;app_version:7.41.0;browser:Chrome;scheme;schema_version:0.0.911', 'x-hs-client-targeting':'ad_id:cb78c6c0-9234-4a1b-a18f-f685afc1705e;user_lat:false', 'x-hs-device-id': Config.HOTSTAR_DEVICE_ID, 'x-hs-platform':'web','x-hs-usertoken': Config.HOTSTAR_USER_TOKEN, 'x-request-id':'1452567'}
        self.COOKIES = {'device_id': Config.HOTSTAR_DEVICE_ID, 'hs_uid': Config.HOTSTAR_DEVICE_ID, 'userLocale': 'eng', 'ajs_group_id': 'null', 'ajs_user_id': '%22cb45c780d2884147a39f6140b3a22b49%22', 'ajs_anonymous_id': '%2205ceb57a-62ea-469d-91f7-9b2105771713%22', 'x_migration_exp': 'true', 'SELECTED__LANGUAGE': 'eng', 'deviceId': Config.HOTSTAR_DEVICE_ID, 'userCountryCode': 'in', '_gcl_au': '1.1.1337394325.1694078636', '_fbp': 'fb.1.1696356044525.311534359', '_ga_VJTFGHZ5NH': 'GS1.2.1696354438.31.1.1696356892.56.0.0', 'userHID': 'edf112b6d22e47288fdede401488e8c8', 'userPID': '75d10f2607bf48b4b5300a1396e7cb02', '_ga': 'GA1.1.1730233636.1678019100', '_uetsid': 'b9823d50718111ee9a76bbf032a61343', '_uetvid':'bf716ff0bb5011ed97134b1236d93e24', 'userUP':Config.HOTSTAR_USER_TOKEN, '_ga_EPJ8DYH89Z': 'GS1.1.1698051247.54.1.1698052065.60.0.0', '_ga_2PV8LWETCX':'GS1.1.1698051247.54.1.1698052065.60.0.0', 'AK_SERVER_TIME': f'{int(time.time())}'}
        self.headers = {'User-Agent': 'Hotstar;in.startv.hotstar/3.3.0 (Android/8.1.0)', 'hotstarauth': self.auth()[0], 'X-Country-Code': 'in', 'X-HS-AppVersion': '3.3.0', 'X-HS-Platform': 'firetv', 'X-HS-UserToken': Config.HOTSTAR_USER_TOKEN, 'Cookie': self.auth()[1]}
        self.infoHeaders = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0', 'Accept': '*/*', 'Accept-Language': 'eng', 'Referer': 'https://www.hotstar.com/', 'x-country-code': 'IN', 'x-platform-code': 'PCTV', 'x-client-code': 'LR', 'hotstarauth': self.auth()[0], 'x-region-code': 'DL', 'x-hs-usertoken': Config.HOTSTAR_USER_TOKEN, 'Origin': 'https://www.hotstar.com', 'DNT': '1', 'Connection': 'keep-alive', 'TE': 'Trailers'}

    def ExtractUrl(self):
        self.raw = self.raw.split(':', 1)
        if len(self.raw) == 2:
            self.SEASON = int(self.raw[0])
            episode = self.raw[1].split('-', 1)
            if len(episode) == 2:
                self.multi_episode = True
                self.from_ep = int(episode[0])
                self.to_ep = int(episode[1])
            else:  # inserted
                self.multi_episode = False
                self.from_ep = int(episode[0])

    def auth(self):
        AKAMAI_ENCRYPTION_KEY = b'\x05\xfc\x1a\x01\xca\xc9K\xc4\x12\xfcS\x12\x07u\xf9\xee'
        st = int(time.time())
        exp = st + 6000
        hotstarauth = 'st=%d~exp=%d~acl=/*' % (st, exp)
        hotstarauth += '~hmac=' + hmac.new(AKAMAI_ENCRYPTION_KEY, hotstarauth.encode(), hashlib.sha256).hexdigest()
        auth = 'hdntl=exp=%d~acl=/*' % exp
        auth += '~data=hdntl~hmac=' + hmac.new(AKAMAI_ENCRYPTION_KEY, hotstarauth.encode(), hashlib.sha256).hexdigest()
        return (hotstarauth, auth)

    def generateDeviceID(self):
        user_token_json = json.loads(base64.b64decode(Config.HOTSTAR_USER_TOKEN.split('.')[1] + '========').decode('utf-8'))
        user_token_json = user_token_json['sub']
        start_index = user_token_json.find('deviceId') + 11
        end_index = user_token_json.find('\",', start_index)
        userDeviceID = user_token_json[start_index:end_index]
        Config.HOTSTAR_DEVICE_ID = userDeviceID
        return userDeviceID

    def refreshUserToken(self):
        user_token_refresh_url = 'https://api.hotstar.com/um/v3/users/refresh'
        token_headers = {'hotstarauth': self.auth()[0], 'deviceid': Config.HOTSTAR_DEVICE_ID, 'x-hs-usertoken': Config.HOTSTAR_USER_TOKEN, 'user-agent': 'Hotstar;in.startv.hotstar/3.3.0 (Android/8.1.0)', 'x-country-code': 'IN', 'x-hs-request-id': Config.HOTSTAR_DEVICE_ID, 'X-HS-Platform': 'web', 'X-HS-Device-Id': Config.HOTSTAR_DEVICE_ID, 'Content-Type': 'application/json; charset=utf-8'}
        user_token_refresh_request = self.session.get(user_token_refresh_url, headers=token_headers)
        self.logger.info(user_token_refresh_request.text)
        user_token_refresh_request = user_token_refresh_request.json()
        refreshed_user_token = user_token_refresh_request['user_identity']
        Config.HOTSTAR_USER_TOKEN = refreshed_user_token
        Config.HOTSTAR_REFRESH = time.time()

    def UpdateUserData(self):
        if int(time.time() - Config.HOTSTAR_REFRESH) > 86400 or Config.HOTSTAR_REFRESH == 0.0:
            self.refreshUserToken()

    def getResponseData(self, url, headers=None):
        try:
            response = self.session.get(url=url, headers=self.infoHeaders if headers == None else headers, proxies=self.proxies)
            jsonData = json.loads(response.content)
            return jsonData
        except:
            self.logger.info(f'error getting data for url: {self.url}')

    def getMovieData(self, contentID):
        for i in range(10):
            movieInfoURL = self.hotstarMovieInfoURL.format(contentID=contentID)
            movieDataJson = self.getResponseData(movieInfoURL, self.infoHeaders)
            try:
                self.title = movieDataJson['body']['results']['item']['name']
              # inserted
                try:
                    self.year = movieDataJson['body']['results']['item']['year']
                except:
                    self.year = ''
                self.logger.info('\nMovie data fetched successfully!!!')
            except Exception as e:
                pass
        self.title = 'Failed to get movie name'
        self.logger.info('\nError fetching Movie Data!!!')

    def getseries(self, contentID):
        showInfoURL = self.hotstarShowInfoURL.format(contentID=contentID)
        showDataJson = self.getResponseData(showInfoURL, self.infoHeaders)
        seasonData = {}
        season_id = None
        try:
            self.title = showDataJson['body']['results']['item']['title']
            for sData in showDataJson['body']['results']['trays']['items']:
                if sData['title'] == 'Seasons':
                    for season in sData['assets']['items']:
                        if self.SEASON == season['seasonNo']:
                            season_id = season['id']
                            break
            self.logger.info('\nSeason data fetched successfully!!!')
        except Exception as e:
            self.logger.info(e)
            self.logger.info('\nError fetching Season Data!!!')
            return
        if season_id is None:
            raise Exception('Season not found')
        seasonInfoURL = self.hotstarSeasonInfoURL.format(seasonID=season_id)
        episodeDataJson = self.getResponseData(seasonInfoURL)
        playlist = []
        try:
            for episode in episodeDataJson['body']['results']['assets']['items']:
                playlist.append({'id': episode['id'], 'number': episode['episodeNo'], 'name': self.title + ' ' + 'S{}E{}'.format(self.FixSeq(season['seasonNo']), self.FixSeq(episode['episodeNo'])), 'contentId': episode['contentId']})
            self.logger.info(f'\nSeason: {season} episode data fetched successfully!!!')
        except:
            self.logger.info(f'{episodeDataJson}')
            self.logger.info(f'\nError fetching Episode Data for Season: {season}!!!')
            return None
        return (self.title, playlist)

    def FixSeq(self, seq):
        if int(len(str(seq))) == 1:
            return f'0{str(seq)}'
        return str(seq)

    def fix_id_ytdl(self, ytid):
        return ytid.replace('/', '_')

    async def parse_m3u8(self, m3u8):
        """It will extract all the data from link"""  # inserted
        try:
            yt_data = ytdl.YoutubeDL({'no-playlist': True, 'geo_bypass_country': 'IN', 'allow_unplayable_formats': True}).extract_info(m3u8, download=False)
            formats = yt_data.get('formats', None)
            data = {}
            data['videos'] = []
            data['audios'] = []
            data['pssh'] = ''
            data['subtitles'] = []
            if formats:
                for i in formats:
                    format_id = i.get('format_id', '')
                    format = i.get('format', '')
                    if 'audio' in format or i.get('audio_ext', 'None') not in ['None', None, 'none', '']:
                        data['audios'].append({'lang': i.get('language', 'default') + f" ({int(i.get('tbr', 56) if i.get('tbr')!= None else 128)}kbps)", 'id': format_id})
                    if not 'video' in format:
                        if i.get('video_ext', 'None') not in ['None', None, 'none', '']:
                            pass  # postinserted
                    data['videos'].append({'height': str(i.get('height', 'default')) + f" ({int(i.get('tbr', 56) if i.get('tbr')!= None else 128)}kbps)", 'id': format_id})
            else:  # inserted
                raise Exception('Error in getting data')
                return data
        except Exception as e:
            raise Exception(e)
 #   import json
#    import xmltodict

    async def parsempd(self, MpdUrl,msg=None):
      if '.mpd' not in MpdUrl or '.m3u8' in MpdUrl:
            return await self.parse_m3u8(MpdUrl)
      else:  # inserted
            audio_list = []
            video_list = []
            subtitle_list = []
            pssh = ''
            print(MpdUrl)
            mpdHeaders = {'Accept-Encoding': 'gzip, deflate', 'User-Agent': 'KAIOS/2.0', 'Accept-Language': 'en-us,en;q=0.5', 'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.7', 'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8'}
            mpd = self.session.get(MpdUrl, headers=mpdHeaders, proxies=self.proxies)
            if mpd.status_code!= 200:
                mpdPath = f'mpd-{time.time()}.txt'
                m = subprocess.run(['aria2c', '--allow-u', MpdUrl, '-o', mpdPath, '-U', 'KAIOS'])
                if os.path.exists(mpdPath):
                    with open(mpdPath) as f:
                        mpd = f.read()
                        f.close()
                    os.remove(mpdPath)
                else:  # inserted
                    self.logger.error('failed downloading mpd with aria2c too..!')
                    raise Exception('')
            else:  # inserted
                mpd = mpd.text
                print(mpd)
#            if mpd:
 #               mpd = re.sub('<!--  -->', '', mpd)
  #              mpd = re.sub('<!-- Created+(..*)', '', mpd)
   #             mpd = re.sub('<!-- Generated+(..*)', '', mpd)
      import xml.etree.ElementTree as ET
#def parse_mpd(mpd_content):
      root = ET.fromstring(mpd)

  #  video_list = []
   # audio_list = []
    #subtitle_list = []
#    pssh = ''

      for period in root.findall('.//{urn:mpeg:dash:schema:mpd:2011}Period'):
        for adaptation_set in period.findall('.//{urn:mpeg:dash:schema:mpd:2011}AdaptationSet'):
            if adaptation_set.attrib['mimeType'] == 'video/mp4':
                for representation in adaptation_set.findall('.//{urn:mpeg:dash:schema:mpd:2011}Representation'):
                    video_dict = {
                        'width': representation.attrib['width'],
                        'height': representation.attrib['height'],
                        'id': representation.attrib['id'],
                        'codec': representation.attrib['codecs'],
                        'bandwidth': representation.attrib['bandwidth']
                    }
                    video_list.append(video_dict)
            elif adaptation_set.attrib['mimeType'] == 'audio/mp4':
                for representation in adaptation_set.findall('.//{urn:mpeg:dash:schema:mpd:2011}Representation'):
                    audio_dict = {
                        'id': representation.attrib['id'],
                        'codec': representation.attrib['codecs'],
                        'bandwidth': representation.attrib['bandwidth'],
                        'lang': adaptation_set.attrib.get('lang', '')
                    }
                    audio_list.append(audio_dict)
            elif adaptation_set.attrib['mimeType'] == 'text/vtt':
                for representation in adaptation_set.findall('.//{urn:mpeg:dash:schema:mpd:2011}Representation'):
                    subtitle_dict = {
                        'lang': representation.attrib['lang'],
                        'id': representation.attrib['id']
                    }
                    subtitle_list.append(subtitle_dict)
            for content_protection in adaptation_set.findall('.//{urn:mpeg:dash:schema:mpd:2011}ContentProtection'):
                if content_protection.attrib['schemeIdUri'] == 'urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed':
                    pssh_element = content_protection.find('.//{urn:mpeg:cenc:2013}pssh')
                    if pssh_element is not None:
                        pssh = pssh_element.text
      video_list.sort(key=lambda x: int(x['bandwidth']))
      audio_list.sort(key=lambda x: int(x['bandwidth']))
#      return video_list, audio_list, subtitle_list, pssh
      return {
            'videos': video_list,
            'audios': audio_list,
            'subtitles': subtitle_list,
            'pssh': pssh
        }









#      try:
        # Parse MPD XML to JSON

 #       mpd = json.loads(json.dumps(xmltodict.parse(mpd)))

        # Check if MPD is actually an M3U8 playlist
#        if '#EXTM3U' in mpd.upper():
 #           return await self.parse_m3u8(MpdUrl)

        # Extract AdaptationSet

        # Determine base URL
  #      baseurl = MpdUrl.rsplit('manifest')[0]



    async def rsempd(self, MpdUrl, msg=None):
        if '.mpd' not in MpdUrl or '.m3u8' in MpdUrl:
            return await self.parse_m3u8(MpdUrl)
        else:  # inserted
            audioslist = []
            videoslist = []
            subtitlelist = []
            pssh = ''
            print(MpdUrl)
            mpdHeaders = {'Accept-Encoding': 'gzip, deflate', 'User-Agent': 'KAIOS/2.0', 'Accept-Language': 'en-us,en;q=0.5', 'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.7', 'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8'}
            mpd = self.session.get(MpdUrl, headers=mpdHeaders, proxies=self.proxies)
            if mpd.status_code!= 200:
                mpdPath = f'mpd-{time.time()}.txt'
                m = subprocess.run(['aria2c', '--allow-u', MpdUrl, '-o', mpdPath, '-U', 'KAIOS'])
                if os.path.exists(mpdPath):
                    with open(mpdPath) as f:
                        mpd = f.read()
                        f.close()
                    os.remove(mpdPath)
                else:  # inserted
                    self.logger.error('failed downloading mpd with aria2c too..!')
                    raise Exception('')
            else:  # inserted
                mpd = mpd.text
            if mpd:
                mpd = re.sub('<!--  -->', '', mpd)
                mpd = re.sub('<!-- Created+(..*)', '', mpd)
                mpd = re.sub('<!-- Generated+(..*)', '', mpd)
            try:
                mpd = json.loads(json.dumps(xmltodict.parse(mpd)))
            except:
                if '#EXTM3U' in mpd.upper():
                    return await self.parse_m3u8(MpdUrl)
                else:  # inserted
                    self.logger.info(str(mpd))
                    raise 'Failed to parse mpd'
                AdaptationSet = mpd['MPD']['Period']['AdaptationSet']
                baseurl = MpdUrl.rsplit('manifest')[0]
                try:
                    for ad in AdaptationSet:
                        if ad['@mimeType'] == 'video/mp4' or ad['@mimeType'] == 'audio/mp4':
                            if 'ContentProtection' not in ad:
                                continue
                            for protections in ad['ContentProtection']:
                                if protections['@schemeIdUri'] == 'urn:uuid:EDEF8BA9-79D6-4ACE-A3C8-27DCD51D21ED' or protections['@schemeIdUri'] == 'urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed':
                                    pssh = protections['cenc:pssh']
                except Exception as e:
                    self.logger.info('Failed to get pssh, probably not encrypted')
                    pssh = ''
            for ad in AdaptationSet:
                if ad['@mimeType'] == 'audio/mp4':
                    try:
                        auddict = {'id': self.fix_id_ytdl(ad['Representation']['@id']), 'codec': ad['Representation']['@codecs'], 'bandwidth': ad['Representation']['@bandwidth'], 'lang': ad.get('@lang', 'Default') + ' ' + f"({fix_codec_name(ad['Representation']['@codecs'])} - {bandwith_convert(ad['Representation']['@bandwidth'])})"}
                        audioslist.append(auddict)
                    except Exception:
                        for item in ad['Representation']:
                            auddict = {'id': self.fix_id_ytdl(item['@id']), 'codec': item['@codecs'], 'bandwidth': item['@bandwidth'], 'lang': ad.get('@lang', 'Default') + ' ' + f"({fix_codec_name(item['@codecs'])} - {bandwith_convert(item['@bandwidth'])})"}
                            audioslist.append(auddict)
                if ad['@mimeType'] == 'video/mp4':
                    for item in ad['Representation']:
                        viddict = {'width': item['@width'], 'height': item['@height'] + f" - {bandwith_convert(item['@bandwidth'])}", 'id': self.fix_id_ytdl(item['@id']), 'codec': item['@codecs'], 'bandwidth': item['@bandwidth']}
                subdict = {'id': self.fix_id_ytdl(ad['Representation']['@id']), 'lang': ad['@lang'], 'bandwidth': ad['Representation']['@bandwidth'], 'url': baseurl + ad['Representation']['BaseURL']}
                continue
            videoslist = sorted(videoslist, key=lambda k: int(k['bandwidth']))
            audioslist = sorted(audioslist, key=lambda x: int(x['bandwidth']))
            all_data = {
                       'videos': videoslist,
                       'audios': audioslist,
                       'subtitles': subtitlelist,
                       'pssh': pssh
                       }
    def Get_PSSH(self, mp4_file):
        WV_SYSTEM_ID = '[ed ef 8b a9 79 d6 4a ce a3 c8 27 dc d5 1d 21 ed]'
        pssh = None
        data = subprocess.check_output(['mp4dump', '--format', 'json', '--verbosity', '1', mp4_file])
        data = json.loads(data)
        for atom in data:
            if atom['name'] == 'moov':
                for child in atom['children']:
                    if child['name'] == 'pssh' and child['system_id'] == WV_SYSTEM_ID:
                        pssh = child['data'][1:(-1)].replace(' ', '')
                        pssh = binascii.unhexlify(pssh)
                        pssh = pssh[0:]
                        pssh = base64.b64encode(pssh).decode('utf-8')
                        return pssh
           # inserted
        return None

    def getWidevineKeys(self, pssh, licurl):
        certData = b'\x08\x04'
        certResponse = self.session.post(url=licurl, data=certData, headers=self.license_headers)
        certDecoded = certResponse.content
        certB64 = base64.b64encode(certDecoded)
        wvdecrypt = WvDecryptCustom(pssh, certB64, deviceconfig.device_nexus6_lvl1, 'tKs4aIW8obCK5fbn5S2GBONlE9vu5qNi_8d-JqhgPQpfChoJd9f==')
        chal = wvdecrypt.get_challenge()
        resp = self.session.post(url=licurl, data=chal, headers=self.license_headers, proxies=self.proxies)
        license_decoded = resp.content
        license_b64 = base64.b64encode(license_decoded)
        wvdecrypt.update_license(license_b64)
        check_, keys = wvdecrypt.start_process()
        if check_:
            return keys
        self.logger.info('Error getting Keys!')
        return []

    async def get_input_data(self):
        """Return:\n           title: str\n           success: True or False\n        """  # inserted
        if self.SEASON:
            _, self.SEASON_IDS = self.getseries(self.mainUrl)
            tempData = self.single(self.SEASON_IDS[self.from_ep - 1].get('contentId'))
        else:  # inserted
            tempData = self.SINGLE = self.single(self.mainUrl)
        if isinstance(tempData, str):
            return (tempData, False)
        mpdUrl, licenseURL, title = tempData
        self.MpdDATA = await self.parsempd(mpdUrl)
        return (title, True)

    async def get_audios_ids(self, key=None):
        """Return list of all available audio streams"""  # inserted
        list_of_audios = []
        if key:
            list_of_audios.append(key)
        for x in self.MpdDATA['audios']:
            list_of_audios.append(x['lang'])
        return list_of_audios

    async def get_videos_ids(self):
        list_of_videos = []
        for x in self.MpdDATA['videos']:
            list_of_videos.append(x['height'])
        return list_of_videos

    def extract_slug(self, url):
        pattern = '/in/([^/]+/[^/]+)/(\\d+)'
        match = re.search(pattern, url)
        if match:
            found_string = '/in/' + match.group(1) + '/' + match.group(2)
            return found_string
        print('Match Not Found')
        return

    def get_serie_id(self, url):
        pattern = '/in/([^/]+/[^/]+)/(\\d+)'
        match = re.search(pattern, url)
        return match.group(2) if match else None

    def single(self, contentID=None, hevc=False):
        if self.xcodec == '4k':
            params = {'filters': 'content_type=episode', 'client_capabilities': '{\"ads\":[\"non_ssai\"],\"audio_channel\":[\"dolbyatmos\",\"dolby51\",\"dolby\",\"stereo\"],\"container\":[\"fmp4\",\"fmp4br\",\"ts\"],\"dvr\":[\"short\"],\"dynamic_range\":[\"sdr\"],\"encryption\":[\"widevine\",\"plain\"],\"ladder\":[\"tv\"],\"package\":[\"dash\",\"hls\"],\"resolution\":[\"4k\",\"fhd\",\"hd\",\"sd\"],\"video_codec\":[\"h265\"],\"audio_codec\":[\"ac4\",\"ec3\",\"aac\"],\"true_resolution\":[\"4k\",\"sd\",\"hd\",\"fhd\"]}', 'drm_parameters': '{\"hdcp_version\":[\"HDCP_V2_2\"],\"widevine_security_level\":[\"SW_SECURE_DECODE\"],\"playready_security_level\":[]}'}
        else:  # inserted
            params = {'filters': 'content_type=episode', 'client_capabilities': '{\"ads\":[\"non_ssai\"],\"audio_channel\":[\"dolbyatmos\",\"dolby51\",\"dolby\",\"stereo\"],\"container\":[\"fmp4\",\"fmp4br\",\"ts\"],\"dvr\":[\"short\"],\"dynamic_range\":[\"sdr\"],\"encryption\":[\"widevine\",\"plain\"],\"ladder\":[\"tv\"],\"package\":[\"dash\",\"hls\"],\"resolution\":[\"4k\",\"fhd\",\"hd\",\"sd\"],\"video_codec\":[\"h264\"],\"audio_codec\":[\"ac4\",\"ec3\",\"aac\"],\"true_resolution\":[\"4k\",\"sd\",\"hd\",\"fhd\"]}', 'drm_parameters': '{\"hdcp_version\":[\"HDCP_V2_2\"],\"widevine_security_level\":[\"SW_SECURE_DECODE\"],\"playready_security_level\":[]}'}
        slug = self.extract_slug(self.url)
        try:
            headers = {'url':self.url,'api':'ottapi'}
            api_url = f'https://www.hotstar.com/api/internal/bff/v2/slugs{slug}/a/{contentID}/watch'
#            response = self.session.get(api_url, params=params, cookies=self.COOKIES, headers=self.HEADERS1).json()
            response = requests.get(url=f"https://hls-proxifier-sage.vercel.app/hotstar?type={type}", headers=headers).json()

        except:
            pass
        if 2<3:  # inserted
            try:
                mpd = response['success']['page']['spaces']['player']['widget_wrappers'][0]['widget']['data']['player_config']['media_asset']['primary']['content_url']
            except:
                self.logger.info(f'Failed to donload mpd: {response}')
        if 2<3:  # inserted
            try:
                license = response['success']['page']['spaces']['player']['widget_wrappers'][0]['widget']['data']['player_config']['media_asset']['primary']['license_url']
            except:
                license = ''
      # postinserted
#        api_url = f'https://www.hotstar.com/api/internal/bff/v2/slugs/in/movies/a/{contentID}/watch'
 #       response = self.session.get(api_url, params=params, cookies=self.COOKIES, headers=self.HEADERS1).json()
  #      try:
   #         mpd = response['success']['page']['spaces']['player']['widget_wrappers'][0]['widget']['data']['player_config']['media_asset']['primary']['content_url']
    #    except:
     #       self.logger.info(f'Failed to donload mpd:')
#        try:
 #           license = response['success']['page']['spaces']['player']['widget_wrappers'][0]['widget']['data']['player_config']['media_asset']['primary']['license_url']
  #      except:
   #         license = ''
        json_ld_data = json.loads(response['success']['page']['spaces']['seo']['widget_wrappers'][0]['widget']['data']['json_ld_data']['schemas'][0])
        showTitle = json_ld_data['name']
        if json_ld_data.get('containsSeason', None) is not None:
            seasonNumber = json_ld_data['containsSeason']['seasonNumber']
            episodeNumber = json_ld_data['containsSeason']['episode']['episodeNumber']
            episodeTitle = json_ld_data['containsSeason']['episode']['name']
            name = f'{showTitle} S{int(seasonNumber):02d}E{int(episodeNumber):02d} {episodeTitle}'
        else:  # inserted
            json_ld_data = json.loads(response['success']['page']['spaces']['seo']['widget_wrappers'][0]['widget']['data']['json_ld_data']['schemas'][1])
            releaseYear = json_ld_data.get('releaseYear', 0)
            name = f'{showTitle} {releaseYear}'
        name = name.replace('(', ' ').replace(')', ' ')
        print(name)
        self.title = name
        return (mpd, license, name)

    async def downloader(self, video, audios, msg=None):
        if not os.path.isdir(self.filedir):
            os.makedirs(self.filedir, exist_ok=True)
        self.msg = msg
        if self.SEASON:
            print("season")
            episodes = []
            seriesname, IDs = self.getseries(self.mainUrl)
            for eps in IDs:
                if self.multi_episode:
                    if int(self.from_ep) <= int(eps.get('number')) <= int(self.to_ep):
                        episodes.append({'contentId': eps.get('contentId'), 'name': eps.get('name'), 'number': eps.get('number')})
                else:  # inserted
                    if int(eps.get('number')) == int(self.from_ep):
                        episodes.append({'contentId': eps.get('contentId'), 'name': eps.get('name'), 'number': eps.get('number')})
            self.COUNT_VIDEOS = len(episodes)
            for x in sorted(episodes, key=lambda k: int(k['number'])):
                url, licenseURL, title = self.single(str(x['contentId']))
                series_name = ReplaceDontLikeWord(unidecode.unidecode(x['name']))
                spisode_number = series_name.rsplit(' ', 1)[1]
                OUTPUT = os.path.join(self.filedir, seriesname)
                OUTPUT = OUTPUT.replace(' ', '.')
                MpdDATA = await self.parsempd(url)
                keys = []
                is_drm = False
                if licenseURL!= '':
                    pssh = MpdDATA['pssh']
                    if pssh!= '':
                        for x in range(5):
#                            keys = self.getWidevineKeys(pssh, licenseURL)
                            keys = requests.get(url='https://hls-proxifier-sage.vercel.app/hs',headers={"url":licenseURL,"pssh":pssh}).json()["keys"]
                            for ke,va in keys.items():
                                keys = f'{ke}:{va}'
                            if keys:
                                break
                            await asyncio.sleep(5)
                    is_drm = True
                downloader = Downloader(url, OUTPUT, 'KAIOS', self.xcodec)
                await downloader.set_data(MpdDATA)
                await self.edit(f'⬇️ **Downloading Episode ...**\n📂 **Filename:** `{spisode_number}-{self.title}`')
                await downloader.download(video, audios)
                await self.edit(f'❇️ **Decrypting Episode ...**\n📂 **Filename:** `{spisode_number}-{self.title}`')
                if is_drm : #or keys == []:
                    video_path = os.path.join(os.getcwd(), downloader.TempPath, 'jv_drm_video_.mkv')
                    pssh = self.Get_PSSH(video_path)
#                    keys = self.getWidevineKeys(pssh, licenseURL)
                    await downloader.set_key(keys)
                    await downloader.decrypt()
                else:  # inserted
                    await downloader.no_decrypt()
                await self.edit(f'🔄 **Muxing Episode ...**\n📂 **Filename:** `{self.title}.{spisode_number}`')
                await downloader.merge(series_name, type_='DSNP')
        else:  # inserted
            self.COUNT_VIDEOS = 1
            print("movie ")
            url, licenseURL, title = self.SINGLE
            keys = []
            is_drm = False
            if licenseURL!= '':
                pssh = self.MpdDATA['pssh']
                if pssh!= '':
                    for x in range(5):
  #                      keys = self.getWidevineKeys(pssh, licenseURL)
                        keys = requests.get(url='https://hls-proxifier-sage.vercel.app/hs',headers={"url":licenseURL,"pssh":pssh}).json()["keys"] #api by aryan chaudhary expired for now
                        for ke,va in keys.items():
                                keys = f'{ke}:{va}'
                        if keys:
                                print(keys)
                                break
                is_drm = True
            OUTPUT = os.path.join(self.filedir, title)
            downloader = Downloader(url, OUTPUT, 'KAIOS', self.xcodec)
            await downloader.set_data(self.MpdDATA)
            await self.edit(f'⬇️ **Downloading ...**\n📂 **Filename:** `{self.title}`')
            await downloader.download(video, audios)
            if is_drm:
                video_path = os.path.join(os.getcwd(), downloader.TempPath, 'jv_drm_video_.mkv')
#                pssh = self.Get_PSSH(video_path)
 #               keys = self.getWidevineKeys(pssh, licenseURL)
                await downloader.set_key(keys)
                await downloader.decrypt()
            else:  # inserted
                await downloader.no_decrypt()
            await self.edit(f'🔄 **Muxing ...**\n📂 **Filename:**  `{self.title}`')
            await downloader.merge(title, type_='DSNP')

    async def edit(self, text):
        try:
            await self.msg.edit(text)
        except:
            return None

class Downloader:
    def __init__(self, mpdUrl, out_path, useragent='', codec=''):
        """url: mpd/m3u8 link\n        key: kid key of drm video"""  # inserted
        self.__url = mpdUrl
        self.__key = None
        self.codec = codec
        self.opts = {'no-playlist': True, 'geo_bypass_country': 'IN', 'allow_unplayable_formats': True}
        self.startTime = str(time.time())
        self.VIDEO_SUFFIXES = ('M4V', 'MP4', 'MOV', 'FLV', 'WMV', '3GP', 'MPG', 'WEBM', 'MKV', 'AVI')
        self.video_file = ''
        self.quality = '480p'
        self.selected_audios = []
        self.log = logging.getLogger(__name__)
        self.downloaded_audios = []
        self.all_data = {}
        self.out_path = out_path
        if useragent == '':
            self.useragent = 'KAIOS'
        else:  # inserted
            self.useragent = useragent
        if not os.path.isdir(self.out_path):
            os.makedirs(self.out_path, exist_ok=True)
        self.TempPath = os.path.join(self.out_path, f'temp.{time.time()}')
        if not os.path.isdir(self.TempPath):
            os.makedirs(self.TempPath)

    async def set_key(self, key):
        self.__key = key

    async def set_data(self, data):
        self.all_data = data

    def fix_id_ytdl(self, ytid):
        return ytid.replace('/', '_')

    async def download_url(self, quality, audio_list, custom_header=[]):
        """Download video and all audio streams using direct url"""  # inserted
        if self.all_data:
            try:
                x = None
                for x in self.all_data['videos']:
                    if x['height'] == quality:
                        x = x['url']
                        break
                    x = None
                if x == None:
                    for x in self.all_data['videos']:
                        if x['height'].lower().startswith(quality.split(' ', 1)[0].lower()):
                            x = x['url']
                            break
                        x = None
                if x == None:
                    qualities = []
                    for x in self.all_data['videos']:
 #                       pass  # postinserted
#                    else:  # inserted
                        try:
                            qualities.append(int(x['height'].split(' ', 1)[0].strip('p')))
                        except:
                            pass
  #                  else:  # inserted
                        try:
                            quality = int(quality.split(' ', 1)[0])
                        except:
                            quality = 480
                        quality = find_nearest_quality(qualities, quality)
                        quality = str(quality)
                        for x in self.all_data['videos']:
                            if x['height'].lower().startswith(quality):
                                x = x['url']
                                break
                            x = None
                if x == None:
                    raise Exception('Quality not found')
                self.quality = quality
                self.selected_audios = audio_list
                self.video_file = os.path.join(os.getcwd(), self.TempPath, '_jv_drm_video.mkv').replace(' ', '.')
                video_download_cmd = ['yt-dlp', '--file-access-retries', '10', '--fragment-retries', '20', '--concurrent-fragments', '5', '--allow-unplayable-formats', '--no-warnings', '--external-downloader', 'aria2c', '--downloader-args', 'aria2c:--retry-wait=1 --max-file-not-found=10 --max-tries=20 -j 500 -x 2', '-o', self.video_file, x]
                if custom_header == []:
#                    video_download_cmd.insert((-3), '--user-agent')
                   # video_download_cmd.insert((-3), self.useragent)
                    video_download_cmd.extend(['--user-agent', self.useragent])
                else:  # inserted
                    for jv in custom_header:
                        video_download_cmd.append(['--add-header',str(jv)])
 #                       video_download_cmd.insert((-3), str(jv))
                if Config.PROXY!= '':
                    video_download_cmd.append( ['--proxy',Config.PROXY])
#                    video_download_cmd.append(Config.PROXY)
                logging.info(video_download_cmd)
                await downloadaudiocli(video_download_cmd)
                if audio_list:
                    for audi in audio_list:

                        try:
                            my_audio = os.path.join(os.getcwd(), self.TempPath, audi.replace('(', '_').replace(')', '_') + '_drm.m4a').replace(' ', '.')
                            audio_format = None
                            for audio_format in self.all_data['audios']:
                                if audio_format['lang'] == audi:
                                    audio_format = audio_format['url']
                                    break
                            if audio_format == None:
                                for audio_format in self.all_data['audios']:
                                    if audio_format['lang'].lower().startswith(audi.split(' ', 1)[0].lower()):
                                        audio_format = audio_format['url']
                                        break
                                    audio_format = None
                #            if audio_format == None:
                 #               pass  # postinserted
#                        except:  # inserted
                            audio_download_cmd = ['yt-dlp', '--add-header', 'range:bytes=0-', '--file-access-retries', '10', '--fragment-retries', '20', '--concurrent-fragments', '5', '--allow-unplayable-formats', '--no-warnings', '--external-downloader', 'aria2c', '--downloader-args', 'aria2c:--retry-wait=1 --max-file-not-found=10 --max-tries=20 -j 500 -x 2', '-o', my_audio, audio_format]
                            if custom_header == []:
                                audio_download_cmd.insert((-3), '--user-agent')
                                audio_download_cmd.insert((-3), self.useragent)
                            else:  # inserted
                                for jv in custom_header:
                                    audio_download_cmd.insert((-3), '--add-header')
                                    audio_download_cmd.insert((-3), str(jv))
                            if Config.PROXY!= '':
                                audio_download_cmd.insert((-3), '--proxy')
                                Config.PROXY()
                            logging.info(audio_download_cmd)
                            await downloadaudiocli(audio_download_cmd)
                #            break
                 #           os.path.basename(my_audio)
                        except Exception as e:
                            pass  # postinserted
   # #                    else:  # inserted
    #                        try:
      #                          pass  # postinserted
       #                     else:  # inserted
        #                        continue
         #       return 0
            except Exception as e:
                pass  # postinserted
   #     else:  # inserted
  #              pass
    #            return None

    async def find_nearest_hi_entry(self, data, bit, target_bandwidth, band):
        hi_entries = [entry for entry in data if bit in entry['lang'].lower()]
        self.log.info(hi_entries)
        nearest_entry = min(hi_entries, key=lambda x: abs(int(band)) - int(target_bandwidth))
        return nearest_entry['id']

    async def nload(self, quality, audio_list, custom_header=[]):
        """Download video with format id and download all audio streams"""  # inserted
        if self.all_data:
            print("data is there")
            try:
                x = None
                for x in self.all_data['videos']:
                    if 'url' in x:
                        await self.download_url(quality, audio_list, custom_header)
   #                 else:  # inserted
                        if x['height'] == quality:
                            x = x['id']
                            break
                        x = None
                if x == None:
                    for x in self.all_data['videos']:
                        if x['height'].lower().startswith(quality.split(' ', 1)[0].lower()):
                            x = x['id']
                            break
                        x = None
                if x == None:
                    qualities = []
                    for x in self.all_data['videos']:
 #                       pass  # postinserted
#                    finally:  # inserted
                        try:
                            qualities.append(int(x['height'].split(' ', 1)[0].strip('p')))
                        except:
                            pass
  #                  finally:  # inserted
                        try:
                            quality = int(quality.split(' ', 1)[0])
                        except:
                            quality = 480
                        quality = find_nearest_quality(qualities, quality)
                        quality = str(quality)
                        for x in self.all_data['videos']:
                            if x['height'].lower().startswith(quality):
                                x = x['id']
                                break
                            x = None
                if x == None or isinstance(x, dict):
                    raise Exception('Quality not found')
                self.quality = quality.split(' ', 1)[0]
                self.selected_audios = audio_list
                self.video_file = os.path.join(os.getcwd(), self.TempPath, '_jv_drm_video.mkv').replace(' ', '.')
                video_download_cmd = ['yt-dlp', '--file-access-retries', '10', '--fragment-retries', '20', '--concurrent-fragments', '5', '--user-agent', self.useragent, '--allow-unplayable-formats', '--format', self.fix_id_ytdl(str(x)), self.__url, '--external-downloader', 'aria2c', '--no-warnings', '--downloader-args', 'aria2c:--retry-wait=1 --max-file-not-found=10 --max-tries=20 -j 500 -x 2', '-o', self.video_file]
                if custom_header!= []:
                    for jv in custom_header:
                        video_download_cmd.insert((-2), '--add-header')
                        video_download_cmd.insert((-2), str(jv))
                if Config.PROXY!= '':
                    video_download_cmd.insert((-2), '--proxy')
                    video_download_cmd.insert((-2), Config.PROXY)
                await downloadaudiocli(video_download_cmd)
                if audio_list:
                    for audi in audio_list:
    #                    pass  # postinserted
     #               finally:  # inserted
                        try:
                            my_audio = os.path.join(os.getcwd(), self.TempPath, audi + '_drm.m4a').replace(' ', '.')
                            audio_format = None
                            for audio_format in self.all_data['audios']:
                                if audio_format['lang'] == audi:
                                    audio_format = audio_format['id']
                                    break
                                audio_format = None
                            if audio_format == None:
                                for audio_format in self.all_data['audios']:
                                    if audio_format['lang'].lower().startswith(audi.split(' ', 1)[0].lower()):
                                        audio_format = audio_format['id']
                                        break
                                audio_format = None
                            if audio_format == None:
                                for audio_format in self.all_data['audios']:
                                    lang = audi.split(' ', 1)[0].lower()
                                    bit = re.search('\\b(\\d+)kbps\\b', audi)
                                    bit_v = bit.group(1)
                                    band = audio_format['bandwidth']
                                    logging.info(bit_v)
                                    logging.info(band)
                                    await self.find_nearest_hi_entry(self.all_data['audios'], lang, bit_v, band)
                                    audio_format = id
                                    logging.info(audio_format)
                            return isinstance(audio_format)
#                            if dict(not audio_format, None):
 #                               pass  # postinserted
       #                 finally:  # inserted
                            audio_download_cmd = ['yt-dlp', '--file-access-retries', '10', '--fragment-retries', '20', '--concurrent-fragments', '5', '--user-agent', self.useragent, '--allow-unplayable-formats', '--format', self.fix_id_ytdl(audio_format), self.__url, '--no-warnings', '--external-downloader', 'aria2c', '--downloader-args', 'aria2c:--retry-wait=1 --max-file-not-found=10 --max-tries=20 -j 500 -x 2', '-o', my_audio]
    #                        if custom_header!= []:
   #                           for jv in custom_header:
      #                          video_download_cmd.insert((-2), '--add-header')
     #                           video_download_cmd.insert((-2), str(jv))
#                            if Config.PROXY!= '':
 #                             video_download_cmd.insert((-2), '--proxy')
  #                            video_download_cmd.insert((-2), Config.PROXY)
                            self.log.info(audio_download_cmd)
                            return await downloadaudiocli(audio_download_cmd)
                            e_res, t_res = (e_res, t_res)
                            audio_download_cmd = ['yt-dlp', '--file-access-retries', '10', '--fragment-retries', '20', '--concurrent-fragments', '5', '--user-agent', self.useragent, '--allow-unplayable-formats', '--format', self.fix_id_ytdl(audio_format), self.__url, '--geo-bypass-country', 'IN', '--no-warnings', '-o', self.video_file]
                            return await downloadaudiocli(video_download_cmd)
                        except:
                            self.downloaded_audios.append(os.path.basename(my_audio))
            except Exception:
                pass

    async def download(self, quality, audio_list, custom_header=None):
      if not self.all_data:
        raise Exception("No data available")

    # Find the video format ID
      video_format_id = None
      for video in self.all_data['videos']:
        if video['height'] == quality:
            video_format_id = video['id']
            break

      if not video_format_id:
        qualities = [int(x['height'].split(' ', 1)[0].strip('p')) for x in self.all_data['videos']]
        quality = int(quality.split(' ', 1)[0])
        nearest_quality = find_nearest_quality(qualities, quality)
        for video in self.all_data['videos']:
            if video['height'].lower().startswith(str(nearest_quality)):
                video_format_id = video['id']
                break

      if not video_format_id:
        raise Exception("Quality not found")

      self.quality = quality.split(' ', 1)[0]
      self.selected_audios = audio_list

    # Download video
      video_downad_cmd = [
        'yt-dlp',
        '--file-access-retries', '10',
        '--fragment-retries', '20',
        '--concurrent-fragments', '5',
        '--user-agent', self.useragent,
        '--allow-unplayable-formats',
        '--no-warnings',
        '--external-downloader', 'ffmpeg',
        '--external-downloader-args', '-loglevel error'
      ]
      self.video_file = os.path.join(os.getcwd(), self.TempPath, 'jv_drm_video_.mkv').replace(' ', '.')
      video_download_cmd = [
    'yt-dlp',
    '--file-access-retries', '10',
    '--fragment-retries', '20',
    '--concurrent-fragments', '5',
    '--user-agent', self.useragent,
    '--allow-unplayable-formats',
    '--format', self.fix_id_ytdl(str(video_format_id)),
    self.__url,
    '--external-downloader', 'aria2c',
    '--no-warnings',
    '--downloader-args',
    'aria2c:--retry-wait=1 --max-file-not-found=10 --max-tries=20 -j 500 -x 2',
    '-o', self.video_file
           ]
      print(self.video_file)
      print(str(video_format_id))

      if custom_header:
        for header in custom_header:
            video_download_cmd.extend(['--add-header', str(header)])

      if Config.PROXY:
        video_download_cmd.extend(['--proxy', Config.PROXY])

      await downloadaudiocli(video_download_cmd)

    # Download audio
      if audio_list:
        for audi in audio_list:
            my_audio = os.path.join(os.getcwd(), self.TempPath, audi + '_drm.m4a').replace(' ', '.')
            audio_format_id = None

            for audio_format in self.all_data['audios']:
                if audio_format['lang'] == audi:
                    audio_format_id = audio_format['id']
                    break

            if not audio_format_id:
                for audio_format in self.all_data['audios']:
                    if audio_format['lang'].lower().startswith(audi.split(' ', 1)[0].lower()):
                        audio_format_id = audio_format['id']
                        break

            if not audio_format_id:
                # Find nearest audio format
                lang = audi.split(' ', 1)[0].lower()
                bit = re.search('\\b(\\d+)kbps\\b', audi)
                bit_v = bit.group(1)
                band = None
                for audio_format in self.all_data['audios']:
                    if audio_format['lang'].lower().startswith(lang):
                        band = audio_format['bandwidth']
                        break
                await self.find_nearest_hi_entry(self.all_data['audios'], lang, bit_v, band)
                audio_format_id = id

            audownload_cmd = [
                'yt-dlp',
                '--file-access-retries', '10',
                '--fragment-retries', '20',
                '--concurrent-fragments', '5',
                '--user-agent', self.useragent,
                '--allow-unplayable-formats',
                '--no-warnings',
                '--external-downloader', 'ffmpeg',
                '--external-downloader-args', '-loglevel error'
            ]
            audio_download_cmd = [
    'yt-dlp',
    '--file-access-retries', '10',
    '--fragment-retries', '20',
    '--concurrent-fragments', '5',
    '--user-agent', self.useragent,
    '--allow-unplayable-formats',
    '--format', self.fix_id_ytdl(audio_format_id),
    self.__url,
    '--no-warnings',
    '--external-downloader', 'aria2c',
    '--downloader-args',
    'aria2c:--retry-wait=1 --max-file-not-found=10 --max-tries=20 -j 500 -x 2',
    '-o', my_audio
               ]
            self.downloaded_audios.append(os.path.basename(my_audio))
            print(my_audio)
            print(audio_format_id)
            if custom_header:
                for header in custom_header:
                    audio_download_cmd.extend(['--add-header', str(header)])

            if Config.PROXY:
                audio_download_cmd.extend(['--proxy', Config.PROXY])

            await downloadaudiocli(audio_download_cmd)

    async def decrypt(self):
        """Decrypt all downloaded streams"""  # inserted
        all_files = self.downloaded_audios + [os.path.basename(self.video_file)]
        temp_audios = []
        for my_file in all_files:
            old_path = os.path.join(os.getcwd(), self.TempPath, my_file)
            old_path = old_path.replace(' ', '.')
            new_path = os.path.join(os.getcwd(), self.TempPath, my_file.replace(' ', '_').rsplit('_', 1)[0].rsplit('.', 1)[0].replace('.', '_') + '_jv.mkv')
            new_path = new_path.replace(' ', '.')
            if old_path.upper().endswith(self.VIDEO_SUFFIXES):
                self.video_file = new_path
            else:  # inserted
                temp_audios.append(new_path)
            cmd = 'mp4decrypt'
            cmd += f' --key {str(self.__key)}'
            cmd += f' \"{old_path}\" \"{new_path}\"'
            st, stout = await run_comman_d(cmd)
            self.log.info(st + stout)
            os.remove(old_path)
        self.downloaded_audios = temp_audios

    async def no_decrypt(self):
        """set all non-drm downloaded streams"""  # inserted
        all_files = self.downloaded_audios + [os.path.basename(self.video_file)]
        temp_audios = []
        for my_file in all_files:
            old_path = os.path.join(os.getcwd(), self.TempPath, my_file)
            old_path = old_path.replace(' ', '.')
            new_path = os.path.join(os.getcwd(), self.TempPath, my_file.replace(' ', '_').rsplit('_', 1)[0].rsplit('.', 1)[0].replace('.', '_') + '_jv.mkv')
            new_path = new_path.replace(' ', '.')
            if old_path.upper().endswith(self.VIDEO_SUFFIXES):
                self.video_file = new_path
            else:  # inserted
                temp_audios.append(new_path)
            os.rename(old_path, new_path)
        self.downloaded_audios = temp_audios

    async def get_info(self, file):
        mediainfo_output = subprocess.Popen(['mediainfo', '--Output=JSON', '-f', file], stdout=subprocess.PIPE)
        return json.load(mediainfo_output.stdout)

    async def merge(self, output_filename, type_='ZEE5'):
        """Merge all downloaded stream"""  # inserted
        if len(self.selected_audios) > 8:
            FORM_DICT = LANGUAGE_FULL_FORM
        else:  # inserted
            FORM_DICT = LANGUAGE_SHORT_FORM
        output_string = [re.sub('\\([^)]*\\)', '', string) for string in self.selected_audios]
        full_forms = {'ta ': 'Tam', 'te ': 'Tel', 'en ': 'Eng', 'hi ': 'Hin', 'bn ': 'Ben', 'kn ': 'Kan', 'mr ': 'Mar', 'ml ': 'Mal', 'Ta ': 'tam', 'Te ': 'Tel', 'En ': 'Eng', 'Be ': 'Ben', 'Kn ': 'Kan', 'Mr ': 'Mar', 'Ml ': 'Mal'}
        match = None
        ad = 'Unknown'
        if len(self.selected_audios) >= 1:
            match = re.search('\\((.*?)\\)', self.selected_audios[0])
        if match:
            ad = match.group(0)
            ad = ad.split(' - ')[(-1)]
        self.quality = self.quality.split(' ', 1)[0]
        full_forms_list = [full_forms.get(abbr, abbr) for abbr in output_string]
        out_file = f'{output_filename} {self.quality}p {type_} WEB-DL [Default] {Config.END_NAME}.mkv'
        if len(self.selected_audios) == 1:
            FORM_DICT = LANGUAGE_FULL_FORM
            out_file = f"{output_filename} {self.quality}p {type_} WEB-DL [{' + '.join((FORM_DICT.get(x.lower(), x.capitalize()) for x in full_forms_list))}  - {ad}] {Config.END_NAME}.mkv"
        else:  # inserted
            if len(self.selected_audios) >= 2:
                out_file = f"{output_filename} {self.quality}p {type_} WEB-DL - {ad}[{' + '.join((FORM_DICT.get(x.lower(), x.capitalize()) for x in full_forms_list))}] {Config.END_NAME}.mkv"
        out_file = out_file.replace(' ', '.')
        out_path = os.path.join(self.out_path, out_file)
        video_path = self.video_file
        cmd = f'ffmpeg -y -i \"{video_path}\" '
        audios = self.downloaded_audios
        for audio in audios:
            cmd += f'-i \"{audio}\" '
        if len(self.downloaded_audios) == 0:
            cmd += '-map 0 '
        else:  # inserted
            cmd += '-map 0:v '
        for i in range(1, len(audios) + 1):
            cmd += f'-map {i}:a? '
        step = 0
        for audio in audios:
            cmd += f'-metadata:s:a:{step} title=\"{Config.METADATA_NAME} - [ - {ad}]\" '
            step += 1
        cmd += f'-c:v copy -c:a copy \"{out_path}\"\n        '
        print(out_path)
        st, stout = await run_comman_d(cmd)

        return
        data = await self.get_info(out_path)
        print(json.dumps(data))
        audio_track = [x for x in data['media']['track'] if x['@type'] == 'Audio']
        audio_track = audio_track[0]
        if audio_track['Format'] == 'E-AC-3':
            codec = 'DD+'
        else:  # inserted
            codec = 'DD'
            if audio_track['Format'] == 'AAC':
                codec = 'AAC'
            else:  # inserted
                if audio_track['Format'] == 'DTS':
                    codec = 'DTS'
                else:  # inserted
                    codec = 'DD+'
        ch = '7.1'
        ch = '5.1'
        v_codec = 'x265'
        ch = '2.0'
        ch = '1.0'
        ch = '5.1'
        ac = '{} {} {}'.format(codec, ch, 'Atmos')
        ac = '{} {}'.format(codec, ch)
        X = [x for x in data['media']['track'] if x['@type'] == 'Video']
        abb = "hi" #ad(video_track, video_track[0])
        cdec = "hello" #video_track[0]['Format'].lower()
        if 'hev' in cdec or 'hvc' in cdec:
            v_codec = 'x265'
        cmd += f'{Config.METADATA_NAME} - [{codec} {ch}- {abb}]\" '
        await run_comman_d(cmd)
        for aud_file in self.downloaded_audios:
            if 2<3:
                os.remove(aud_file)
        if 2<3:
            os.remove(self.video_file)
        return None
