# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('UFVQuestAPI', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='questtype',
            name='alias',
            field=models.CharField(max_length=10, null=True),
            preserve_default=True,
        ),
    ]
