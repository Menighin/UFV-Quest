# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('ufv_quest_api', '0002_auto_20141016_1829'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='gotoandanswer',
            name='questtype_ptr',
        ),
        migrations.RemoveField(
            model_name='seekandanswer',
            name='questtype_ptr',
        ),
        migrations.AddField(
            model_name='gotoandanswer',
            name='quest_ptr',
            field=models.OneToOneField(parent_link=True, auto_created=True, primary_key=True, default=1, serialize=False, to='ufv_quest_api.Quest'),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='seekandanswer',
            name='quest_ptr',
            field=models.OneToOneField(parent_link=True, auto_created=True, primary_key=True, default=1, serialize=False, to='ufv_quest_api.Quest'),
            preserve_default=False,
        ),
    ]
