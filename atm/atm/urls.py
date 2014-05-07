from django.conf.urls import patterns, include, url
from magic.views import *
from django.contrib import admin
from atm.views import *
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'atm.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    (r'^gracze/', include('magic.urls')),
   # url(r'^hello_template/$', 'magic.views.hello_template'),
   # url(r'^hello_class_view/$', HelloTemplate.as_view()),
   # url(r'^hello_template_simple/$', 'magic.views.hello_template_simple'),   
   # url(r'^hello/$', 'magic.views.hello'),
#    (r'^accounts/', include('userprofile.urls')),
    url(r'^authorize/', 'atm.views.authorize'),
    url(r'^accounts/send/$', 'atm.views.send_email'),
   #url(r'^admin/', include(admin.site.urls)),
    url(r'^admin_page/', 'atm.views.admin_page'),
    url(r'^log/','atm.views.log'),
    #url(r'^/rozgrywka/(?P<part>\d+)/$', 'magic.views.gra'),
    url(r'^stat/$', 'magic.views.stat'),
    url(r'^$', 'magic.views.main'),
    url(r'^main/$', 'magic.views.main'),
    url(r'^about/$', 'magic.views.about'),
    url(r'^download/$', 'magic.views.download'),
    url(r'^info/$', 'magic.views.info'),
    url(r'^graf/$', 'magic.views.graf'),
    url(r'^video/$', 'magic.views.video'),
    #url(r'^/login/$', 'magic.views.login'),
    url(r'^stat/popular/$', 'magic.views.popular'),
    url(r'^stat/leng/$', 'magic.views.leng'),
    url(r'^stat/color/$', 'magic.views.color'),
    url(r'^stat/best/$', 'magic.views.best'),
    url(r'^accounts/login/$', 'atm.views.login'),
    url(r'^accounts/auth/$', 'atm.views.auth_view'),
    url(r'^accounts/logout/$', 'atm.views.logout'),
    url(r'^accounts/logged_in/$', 'atm.views.logged_in'),
    url(r'^accounts/invalid/$', 'atm.views.invalid_login'),    
    url(r'^accounts/register/$', 'atm.views.register_user'),
    url(r'^accounts/register_success/$', 'atm.views.register_success'),

#    url(r'^create/$', 'magic.views.create'),
)
