package com.mle.audio

import com.mle.audio.meta.SongMeta

/**
 * @author Michael
 */
trait IPlaylist {
  def songList: Seq[SongMeta]

  def index: Int

  def index_=(newIndex: Int)

  def current: Option[SongMeta]

  def next: Option[SongMeta]

  def prev: Option[SongMeta]

  def add(song: SongMeta)

  def delete(pos: Int)

  def clear()
}
