#!/usr/bin/env python

import cssselect
import lxml.html
import lxml.etree
import pprint
import urllib2

class InfoqPresentation:
  """Extract metadata from a InfoQ presentation website."""
  host  = 'www.infoq.com'
  agent = 'Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) ' \
    'AppleWebKit/536.26 (KHTML, like Gecko) ' \
    'Version/6.0 Mobile/10A403 Safari/8536.25'

  def __init__(self, id):
    self.id   = '/presentations/' + id
    self.link = ''
    self.html = ''
    self.meta = {}

  def __str__(self):
    pp = pprint.PrettyPrinter(indent=2)
    return pp.pformat(self.meta)
  
  def css_select(self, html, sel):
    # document = lxml.html.fromstring(html) # deprecated?
    # return document.cssselect(sel)        # deprecated?
    document = lxml.html.document_fromstring(html)
    selector = cssselect.HTMLTranslator().css_to_xpath(sel)
    return document.xpath(selector)
  
  def fetch_url(self, url, agent):
    request = urllib2.Request(url)
    agent and request.add_header('User-agent', agent)
    response = urllib2.urlopen(request)
    html = response.read()
    response.close()
    return html
  
  def parse(self):
    url = 'http://%s%s' % (self.host, self.id)
    self.link  = url
    self.html = self.fetch_url(url, self.agent)
    return self
  
  def metadata(self):
    self.meta = {
      'id'          : self.id,
      'title'       : self.title(),
      'link'        : self.link,
      'authors'     : self.authors(),
      'date'        : self.date(),
      'description' : self.summary(),
      'video_url'   : self.video_url(),
      'video_len'   : self.video_len(),
      'audio_url'   : self.audio_url(),
      'slides_url'  : self.slides_url(),
      'slides'      : self.slides()
    }
    return self

  def title(self):
    sel = 'h1.general > div'
    res = self.css_select(self.html, sel)
    return res[0].text

  def authors(self):
    sel = '.author_general > a'
    res = self.css_select(self.html, sel)
    return [author.strip() for author in res[0].text.split(',')]

  def date(self):
    sel = '.author_general'
    res = self.css_select(self.html, sel)
    innerText = lambda x: x.xpath('text()')[1] # Skip inner tag(s)
    return innerText(res[0]).replace('on', '').strip()

  def summary(self):
    sel = '#summary'
    res = self.css_select(self.html, sel)
    innerText = lambda x: x.xpath('text()')[1] # Skip inner tag(s)
    return innerText(res[0]).strip()

  def video_url(self):
    sel = 'video#video > source[src]'
    res = self.css_select(self.html, sel)
    return res[0].get('src')

  def video_len(self):
    sel = '.videolength2'
    res = self.css_select(self.html, sel)
    return res[0].text
  
  def audio_url(self):
    # http://res.infoq.com/downloads/mp3downloads/presentations/infoq-13-jun-maryhadalittle.mp3
    # <form method="post" action="/mp3download.action" target="_blank" id="mp3Form">
    #   <input type="hidden" name="filename" value="presentations/infoq-13-mar-macrosvsmonads.mp3"/>
    # </form>
    sel  = '#mp3Form > input'
    res  = self.css_select(self.html, sel)
    val  = self.link + res[0].get('value')
    url  = 'http://%s/mp3download.action' % self.host
    html = self.fetch_url(url, {'filename': val})
  
  def slides_url(self):
    # http://res.infoq.com/downloads/pdfdownloads/presentations/QConNY2013-StephenChin-MaryHadLittleLambda.pdf
    # <form method="post" action="/pdfdownload.action" target="_blank" id="pdfForm">
    #   <input type="hidden" name="filename" value="presentations/ClojureWest2013-ClaggettChouser-MacrosvsMonads.pdf"/>
    # </form>
    pass
    # sel = ''
    # res = self.css_select(self.html, sel)
    # return res[0].get('')
  
  def slides(self):
    # /resource/presentations/Macros-Monads/en/slides/sl1.jpg
    pass
    # sel = ''
    # res = self.css_select(self.html, sel)
    # return res[0].get('')
  
  
def main(id):
  print(InfoqPresentation(id).parse().metadata())
  
  
if __name__ == '__main__':
  main('job-interview-tips')
  main('java-8-lambda-qcon-ny-2013')
  main('DSL-Clojure')
  main('Clojure-Data-Reader')
  main('DDD-Clojure')
  main('Cascalog-Hadoop')
  main('code-review-critique')
  main('clojure-infrastructure')
  main('Macros-Monads')
  
